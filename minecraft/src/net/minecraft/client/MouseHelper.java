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

	public MouseHelper(Component var1) {
		this.windowComponent = var1;

		try {
			this.robot = new Robot();
		} catch (AWTException var4) {
			var4.printStackTrace();
		}

		IntBuffer var5 = BufferUtils.createIntBuffer(1);
		var5.put(0);
		var5.flip();
		IntBuffer var2 = BufferUtils.createIntBuffer(1024);

		try {
			this.cursor = new Cursor(32, 32, 16, 16, 1, var2, var5);
		} catch (LWJGLException var3) {
			var3.printStackTrace();
		}
	}

	public final void grabMouseCursor() {
		try {
			Mouse.setNativeCursor(this.cursor);
		} catch (LWJGLException var2) {
			var2.printStackTrace();
		}

		this.ungrabMouseCursor();
		this.deltaX = 0;
		this.deltaY = 0;
	}

	public final void ungrabMouseCursor() {
		Point var1 = MouseInfo.getPointerInfo().getLocation();
		Point var2 = this.windowComponent.getLocationOnScreen();
		this.robot.mouseMove(this.mouseX, this.mouseY);
		this.mouseX = var2.x + this.windowComponent.getWidth() / 2;
		this.mouseY = var2.y + this.windowComponent.getHeight() / 2;
		if(this.mouseInt == 0) {
			this.deltaX = var1.x - this.mouseX;
			this.deltaY = var1.y - this.mouseY;
		} else {
			this.deltaX = this.deltaY = 0;
			--this.mouseInt;
		}
	}
}
