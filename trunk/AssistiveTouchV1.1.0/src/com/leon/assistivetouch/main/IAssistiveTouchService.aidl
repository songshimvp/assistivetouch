package com.leon.assistivetouch.main;

interface IAssistiveTouchService {
	boolean isRunning();
	void start();
	void stop();
	void refresh();
}