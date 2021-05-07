package io.github.pranavgade20.fabrichax.scripting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.LinkedList;

public class ScriptScreen extends Screen {
    final int LINE_HEIGHT = 10;
    final int SCRIPT_X_OFFSET = 10;
    final int SCRIPT_Y_OFFSET = 30;

    Screen parent;
    ScriptBase scriptBase;

    class Pair<A, B> {
        public A left;
        public B right;
        public Pair(A left, B right) {
            this.left = left;
            this.right = right;
        }
    }
    Pair<Integer, Integer> cursor = new Pair<>(0, 0);
    int tickCounter = 0;
    protected ScriptScreen(Text title, Screen parent, ScriptBase scriptBase) {
        super(title);
        this.parent = parent;
        this.scriptBase = scriptBase;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        int x = 10;
        int y = 30;
        for (String str : scriptBase.getScript()) {
            this.textRenderer.draw(matrices, str, x, y, -1);
            y += LINE_HEIGHT;
        }

        if (tickCounter / 6 % 2 == 0) {
            int cursor_x = this.textRenderer.getWidth(scriptBase.getScript().get(cursor.right).substring(0, cursor.left)) + SCRIPT_X_OFFSET;
            int cursor_y = cursor.right * LINE_HEIGHT + SCRIPT_Y_OFFSET;

            DrawableHelper.fill(matrices, cursor_x, cursor_y, cursor_x + 1, cursor_y + 9, 0xFFEEEEEE);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        MinecraftClient.getInstance().openScreen(parent);
    }

    @Override
    protected void init() {
        int x = 10;
        int y = 30;
    }

    @Override
    public void tick() {
        super.tick();
        ++this.tickCounter;
    }

    void backspace() {
        LinkedList<String> script = scriptBase.getScript();
        String t = script.remove(cursor.left.intValue());

        script.add(cursor.left, t.substring(0, cursor.right-1) + t.substring(cursor.right));
        cursor.right--;
    }
    void delete() {
        LinkedList<String> script = scriptBase.getScript();
        String t = script.remove(cursor.left.intValue());

        script.add(cursor.left, t.substring(0, cursor.right) + t.substring(cursor.right+1));
    }
    void insertChar(char ch) {
        LinkedList<String> script = scriptBase.getScript();
        String t = script.remove(cursor.left.intValue());
        if (ch == '\n') {
            script.add(cursor.left, t.substring(0, cursor.right) + "\n");
            cursor.left++;
            script.add(cursor.left, t.substring(cursor.right));
            cursor.right = 0;
        } else {
            script.add(cursor.left, t.substring(0, cursor.right) + ch + t.substring(cursor.right));
            cursor.right++;
        }
    }
    void moveCursorVertically(int d, boolean isShiftDown) {
        cursor.left += d;
        cursor.left = Math.min(cursor.left, scriptBase.script.size());
        cursor.left = Math.max(cursor.left, 0);
    }
    void moveCursorHorizontally(int d, boolean isShiftDown) {
        cursor.right += d;
        cursor.right = Math.min(cursor.right, scriptBase.script.get(cursor.right.intValue()).length());
        cursor.right = Math.max(cursor.right, 0);
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) return true;

        switch(keyCode) {
            case 257:
            case 335:
                this.insertChar('\n');
                return true;
            case 259:
                this.backspace();
                return true;
            case 261:
                this.delete();
                return true;
            case 262:
                this.moveCursorHorizontally(1, Screen.hasShiftDown());
                return true;
            case 263:
                this.moveCursorHorizontally(-1, Screen.hasShiftDown());
                return true;
            case 264:
                this.moveCursorVertically(-1, Screen.hasShiftDown());
                return true;
            case 265:
                this.moveCursorVertically(1, Screen.hasShiftDown());
                return true;
//            case 266:
//                this.previousPageButton.onPress();
//                return true;
//            case 267:
//                this.nextPageButton.onPress();
//                return true;
//            case 268:
//                this.moveCursorToTop();
//                return true;
//            case 269:
//                this.moveCursorToBottom();
//                return true;
            default:
                return false;
        }
    }
}
