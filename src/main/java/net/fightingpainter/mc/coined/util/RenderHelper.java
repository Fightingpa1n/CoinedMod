package net.fightingpainter.mc.coined.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/** Helper class for rendering Stuff */
public class RenderHelper { //TODO: add comments to all methods
    
    //============================== Texture Rendering ==============================\\
    //=========== Whole Texture ===========\\
    public static void renderTexture(GuiGraphics graphics, ResourceLocation texture, int textureWidth, int textureHeight, int x, int y) {
        graphics.blit(texture, x, y, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }
    
    public static void renderAdvancedTexture(ResourceLocation texture, int width, int height, float x, float y) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        
        bufferBuilder.addVertex(x, y, 0.0f).setUv(0, 0);
        bufferBuilder.addVertex(x, y + height, 0.0f).setUv(0, 1);
        bufferBuilder.addVertex(x + width, y + height, 0.0f).setUv(1, 1);
        bufferBuilder.addVertex(x + width, y, 0.0f).setUv(1, 0);
        
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    //=========== Part of a Texture ===========\\
    public static void renderTexture(GuiGraphics graphics, ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int width, int height, int x, int y) {
        graphics.blit(texture, x, y, u, v, width, height, textureWidth, textureHeight);
    }

    public static void renderAdvancedTexture(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v, int width, int height, float x, float y) {
        float u0 = (float) u / textureWidth;
        float v0 = (float) v / textureHeight;
        float u1 = (float) (u + width) / textureWidth;
        float v1 = (float) (v + height) / textureHeight;
        
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        
        bufferBuilder.addVertex(x, y, 0.0f).setUv(u0, v0);
        bufferBuilder.addVertex(x, y + height, 0.0f).setUv(u0, v1);
        bufferBuilder.addVertex(x + width, y + height, 0.0f).setUv(u1, v1);
        bufferBuilder.addVertex(x + width, y, 0.0f).setUv(u1, v0);
        
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }


    //============================== Text Rendering ==============================\\ //TODO: make the text rendering methods more unified
    public static Font getFont() {
        return Minecraft.getInstance().font; //get the default font
    }
    
    public static void renderText(GuiGraphics graphics, Component text, Font font, int color, boolean shadow, int x, int y) {
        graphics.drawString(font, text, x, y, color, shadow);
    }

    public static void renderText(GuiGraphics graphics, Component text, int color, boolean shadow, int x, int y) {
        renderText(graphics, text, getFont(), color, shadow, x, y);
    }

    public static void renderText(GuiGraphics graphics, String text, Font font, int color, boolean shadow, int x, int y) {
        graphics.drawString(font, text, x, y, color, shadow);
    }

    public static void renderText(GuiGraphics graphics, String text, Font font, int color, boolean shadow, float x, float y) {
        graphics.drawString(font, text, x, y, color, shadow);
    }

    public static float getTextWidth(Font font, float scale, String text) {
        return font.width(text) * scale;
    }

    public static float getTextHeight(Font font, float scale) {
        return font.lineHeight * scale;
    }

    public static void renderCenteredText(GuiGraphics graphics, Font font, String text, float x, float y, int color, float scale) {
        float textWidth = getTextWidth(font, scale, text); //get text width
        float textHeight = getTextHeight(font, scale); //get text height
        graphics.pose().pushPose(); //push pose
        graphics.pose().scale(scale, scale, 1); //scale
        graphics.drawString(font, text, ((x-(textWidth/2)) / scale), ((y-(textHeight/2)) / scale), color, false); //draw centered text
        graphics.pose().popPose(); //pop pose    
    }


    //============================== Alpha ==============================\\
    //=========== Normal Alpha toggeling ===========\\
    public static void toggleAlpha(boolean alpha) {
        if (alpha) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        } else {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    }

    public static void enableAlpha() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void disableAlpha() {
        RenderSystem.disableBlend();
    }

    public static void renderWithAlpha(Runnable runnable) {
        enableAlpha();
        runnable.run();
        disableAlpha();
    }

    //=========== Specific Alpha value ===========\\
    public static void setAlpha(float alpha) {
        enableAlpha();
        RenderSystem.setShaderColor(1, 1, 1, alpha);
    }

    public static void resetAlpha() {
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public static void renderWithAlpha(float alpha, Runnable runnable) {
        setAlpha(alpha);
        runnable.run();
        resetAlpha();
        disableAlpha();
    }


    //============================== Scale ==============================\\
    //=========== X/Y Scaling ===========\\
    public static void setScale(GuiGraphics graphics, float xScale, float yScale) {
        graphics.pose().pushPose();
        graphics.pose().scale(xScale, yScale, 1);
    }

    public static void resetScale(GuiGraphics graphics) {
        graphics.pose().popPose();
    }

    public static void renderWithScale(GuiGraphics graphics, float xScale, float yScale, Runnable runnable) {
        setScale(graphics, xScale, yScale);
        runnable.run();
        resetScale(graphics);
    }


    //=========== Square/Equal/Uniform Scaling ===========\\
    public static void setScale(GuiGraphics graphics, float scale) {
        setScale(graphics, scale, scale);
    }

    public static void renderWithScale(GuiGraphics graphics, float scale, Runnable runnable) {
        setScale(graphics, scale);
        runnable.run();
        resetScale(graphics);
    }


    //TODO: add color support
    //TODO: add fill support
    //TODO: add z-axis support
}
