package net.minecraft.client;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public final class MouseHelper {
	private Component windowComponent;
	private Robot robot;
	private int mouseX;
	private int mouseY;
	private Cursor cursor;
	public int deltaX;
	public int deltaY;
	private int mouseInt = 10;

	public MouseHelper(Component component) {
		this.windowComponent = component;

		try {
			this.robot = new Robot();
		} catch (AWTException aWTException4) {
			aWTException4.printStackTrace();
		}

		IntBuffer component1;
		(component1 = BufferUtils.createIntBuffer(1)).put(0);
		component1.flip();
		IntBuffer intBuffer2 = BufferUtils.createIntBuffer(1024);

		try {
			this.cursor = new Cursor(32, 32, 16, 16, 1, intBuffer2, component1);
		} catch (LWJGLException lWJGLException3) {
			lWJGLException3.printStackTrace();
		}
	}

	public final void grabMouseCursor() {
		try {
			Mouse.setNativeCursor(this.cursor);
		} catch (LWJGLException lWJGLException2) {
			lWJGLException2.printStackTrace();
		}

		this.ungrabMouseCursor();
		this.deltaX = 0;
		this.deltaY = 0;
	}

	public final void ungrabMouseCursor() {
		Point point1 = MouseInfo.getPointerInfo().getLocation();
		Point point2 = this.windowComponent.getLocationOnScreen();
		this.robot.mouseMove(this.mouseX, this.mouseY);
		this.mouseX = point2.x + this.windowComponent.getWidth() / 2;
		this.mouseY = point2.y + this.windowComponent.getHeight() / 2;
		if(this.mouseInt == 0) {
			this.deltaX = point1.x - this.mouseX;
			this.deltaY = point1.y - this.mouseY;
		} else {
			this.deltaX = this.deltaY = 0;
			--this.mouseInt;
		}
	}
}