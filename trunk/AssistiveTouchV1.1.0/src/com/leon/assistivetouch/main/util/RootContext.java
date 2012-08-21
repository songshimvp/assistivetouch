package com.leon.assistivetouch.main.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.content.Context;

/** 
 * 类名      RootContext.java
 * 说明  获取root权限
 * 创建日期 2012-8-21
 * 作者  LiWenLong
 * Email lendylongli@gmail.com
 * 更新时间  $Date$
 * 最后更新者 $Author$
*/
public class RootContext {
	private static RootContext instance = null;
	private static Object mLock = new Object();
	String mShell;
	OutputStream o;
	Process p;

	private RootContext(String cmd) throws Exception {
		this.mShell = cmd;
		init();
	}
	
	public static RootContext getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (mLock) {
			try {
				instance = new RootContext("su");
			} catch (Exception e) {
				while (true)
					try {
						instance = new RootContext("/system/xbin/su");
					} catch (Exception e2) {
						try {
							instance = new RootContext("/system/bin/su");
						} catch (Exception e3) {
							e3.printStackTrace();
						}
					}
			}
			return instance;
		}
	}

	private void init() throws Exception {
		if ((this.p != null) && (this.o != null)) {
			this.o.flush();
			this.o.close();
			this.p.destroy();
		}
		this.p = Runtime.getRuntime().exec(this.mShell);
		this.o = this.p.getOutputStream();
	}

	private void system(String cmd) {
		try {
			this.o.write(("LD_LIBRARY_PATH=/vendor/lib:/system/lib "
					+ cmd + "\n").getBytes("ASCII"));
			return;
		} catch (Exception e) {
			while (true)
				try {
					init();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		}
	}

	public void runCommand(String cmd) {
		system(cmd);
	}

	/**
	 * 判断是否已经root了 
	 * */
	public static boolean hasRootAccess(Context ctx) {
		final StringBuilder res = new StringBuilder();
		try {
			if (runCommandAsRoot(ctx, "exit 0", res) == 0)
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 以root的权限运行命令
	 * */
	public static int runCommandAsRoot(Context ctx, String script,
			StringBuilder res) {
		final File file = new File(ctx.getCacheDir(), "secopt.sh");
		final ScriptRunner runner = new ScriptRunner(file, script, res);
		runner.start();
		try {
			runner.join(40000);
			if (runner.isAlive()) {
				runner.interrupt();
				runner.join(150);
				runner.destroy();
				runner.join(50);
			}
		} catch (InterruptedException ex) {
		}
		return runner.exitcode;
	}

	private static final class ScriptRunner extends Thread {
		private final File file;
		private final String script;
		private final StringBuilder res;
		public int exitcode = -1;
		private Process exec;

		public ScriptRunner(File file, String script, StringBuilder res) {
			this.file = file;
			this.script = script;
			this.res = res;
		}

		@Override
		public void run() {
			try {
				file.createNewFile();
				final String abspath = file.getAbsolutePath();
				Runtime.getRuntime().exec("chmod 777 " + abspath).waitFor();
				final OutputStreamWriter out = new OutputStreamWriter(
						new FileOutputStream(file));
				if (new File("/system/bin/sh").exists()) {
					out.write("#!/system/bin/sh\n");
				}
				out.write(script);
				if (!script.endsWith("\n"))
					out.write("\n");
				out.write("exit\n");
				out.flush();
				out.close();

				exec = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(exec.getOutputStream());
				os.writeBytes(abspath);
				os.flush();
				os.close();

				InputStreamReader r = new InputStreamReader(
						exec.getInputStream());
				final char buf[] = new char[1024];
				int read = 0;
				while ((read = r.read(buf)) != -1) {
					if (res != null)
						res.append(buf, 0, read);
				}

				r = new InputStreamReader(exec.getErrorStream());
				read = 0;
				while ((read = r.read(buf)) != -1) {
					if (res != null)
						res.append(buf, 0, read);
				}

				if (exec != null)
					this.exitcode = exec.waitFor();
			} catch (InterruptedException ex) {
				if (res != null)
					res.append("\nOperation timed-out");
			} catch (Exception ex) {
				if (res != null)
					res.append("\n" + ex);
			} finally {
				destroy();
			}
		}

		public synchronized void destroy() {
			if (exec != null)
				exec.destroy();
			exec = null;
		}
	}
}
