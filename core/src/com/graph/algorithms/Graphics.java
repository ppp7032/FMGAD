package com.graph.algorithms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import java.util.ArrayList;

public abstract class Graphics {

  public static final float scaleFactor = Gdx.graphics.getHeight() / 720f;
  public static final BitmapFont[] fonts = new BitmapFont[]{
      Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 10f * scaleFactor, 0),
      Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 12f * scaleFactor, 0),
      Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 15f * scaleFactor, 0),
      Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 20f * scaleFactor, 0),
      Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 25f * scaleFactor, 0),
      Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 26f * scaleFactor, 0),
      Text.generateFont("fonts/DmMono/DmMonoMedium.ttf", 40f * scaleFactor,
          (int) Graphics.scaleFactor)
  };
  public static final Skin[] skins = new Skin[]{
      generateSkin(fonts[1]),
      generateSkin(fonts[2]),
      generateSkin(fonts[3]),
      generateSkin(fonts[5])
  };

  public static void setUpStartVertexInput(final TextField startVertexInput, final float x) {
    startVertexInput.setVisible(false);
    startVertexInput.setAlignment(1);
    startVertexInput.setX(327 * Graphics.scaleFactor + x);
    startVertexInput.setY(479 * Graphics.scaleFactor);
    startVertexInput.setWidth(88 * Graphics.scaleFactor);
    startVertexInput.setHeight(24 * Graphics.scaleFactor);
  }

  public static void drawRectangleWithBorder(final ShapeRenderer renderer, final float x,
      final float y, final float width, final float height, final float borderWidth,
      final float[] colour) {
    final Color previousColor = new Color(renderer.getColor());
    renderer.setColor(colour[0], colour[1], colour[2], colour[3]);
    renderer.rect(x, y, width, height);
    renderer.setColor(0, 0, 0, 1);
    renderer.rectLine(x, y, x + width, y, borderWidth);
    renderer.rectLine(x, y, x, y + height, borderWidth);
    renderer.rectLine(x, y + height, x + width, y + height, borderWidth);
    renderer.rectLine(x + width, y, x + width, y + height, borderWidth);
    renderer.setColor(previousColor);
  }

  public static void drawSelectionMenu(final SpriteBatch spriteBatch, final Texture background,
      final ShapeRenderer shapeRenderer, final Stage stage, final int numberOfAttributes) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    spriteBatch.begin();
    spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    spriteBatch.end();
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    Graphics.drawMenu(numberOfAttributes, shapeRenderer);
    shapeRenderer.end();
    stage.act();
    stage.draw();
  }

  public static void renderGraphEdges(final ShapeRenderer shapeRenderer, final Graph graph) {
    shapeRenderer.setColor(1, 0, 0, 1);
    for (int a = 0; a < graph.getNumberOfVertices(); a++) {
      for (int b = 0; b < graph.getNumberOfEdgesConnectedToVertex(a); b++) {
        Graphics.renderEdge(graph.getXCoordinateOfVertex(a), graph.getYCoordinateOfVertex(a),
            graph.getXCoordinateOfVertex(graph.getVertex(a, b)),
            graph.getYCoordinateOfVertex(graph.getVertex(a, b)), shapeRenderer, graph.isDigraph());
      }
    }
  }

  public static void renderAlert(final ShapeRenderer shapeRenderer, final Text alertMessage) {
    final float width = alertMessage.getWidth() + 24f * Graphics.scaleFactor;
    final float height = alertMessage.getHeight() + 24f * Graphics.scaleFactor;
    Graphics.drawRectangleWithBorder(shapeRenderer, (Gdx.graphics.getWidth() - width) / 2f,
        (Gdx.graphics.getHeight() - height) / 2f, width, height, 2f, new float[]{1, 1, 1, 1});
  }

  private static void renderVertex(final ShapeRenderer shapeRenderer, final Batch batch,
      final Graph graph, final int vertex, final Text vertexLabel) {
    renderVertex(shapeRenderer, batch, graph.getXCoordinateOfVertex(vertex),
        graph.getYCoordinateOfVertex(vertex), vertexLabel);
  }

  public static void renderVertex(final ShapeRenderer shapeRenderer, final Batch batch,
      final float x, final float y, final Text vertexLabel) {
    shapeRenderer.circle(x * Graphics.scaleFactor, y * Graphics.scaleFactor,
        15 * Graphics.scaleFactor);
    shapeRenderer.setColor(247f / 255f, 247f / 255f, 247f / 255f, 1);
    shapeRenderer.circle(x * Graphics.scaleFactor, y * Graphics.scaleFactor,
        13 * Graphics.scaleFactor);
    shapeRenderer.end();
    batch.begin();
    vertexLabel.setTextPosition(x * Graphics.scaleFactor, y * Graphics.scaleFactor, 0, 0);
    vertexLabel.draw(batch, 0);
    batch.end();
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
    shapeRenderer.setColor(0, 0, 0, 1);
  }

  public static void renderGraphVertices(final ShapeRenderer shapeRenderer, final Graph graph,
      final ArrayList<Text> vertexLabels, Batch batch, final int vertexBeingMoved) {
    shapeRenderer.setColor(0, 0, 0, 1);
    for (int a = graph.getNumberOfVertices() - 1; a >= 0; a--) {
      if (vertexBeingMoved != a) {
        renderVertex(shapeRenderer, batch, graph, a, vertexLabels.get(a));
      }
    }
    if (vertexBeingMoved != -1) {
      renderVertex(shapeRenderer, batch, graph, vertexBeingMoved,
          vertexLabels.get(vertexBeingMoved));
    }
  }

  public static Skin generateSkin(final BitmapFont font) {
    final Skin skin = new Skin();
    skin.add("font", font, BitmapFont.class);
    skin.addRegions(new TextureAtlas("skins/cloud-form/skin/cloud-form-ui.atlas"));
    skin.load(Gdx.files.internal("skins/cloud-form/skin/cloud-form-ui.json"));
    return skin;
  }

  private static float[] rotatePointAboutPoint(final float[] point, final float[] centre,
      final float angle) {
    return new float[]{
        (float) (Math.cos(angle) * (point[0] - centre[0]) - Math.sin(angle) * (point[1] - centre[1])
            + centre[0]),
        (float) (Math.sin(angle) * (point[0] - centre[0]) + Math.cos(angle) * (point[1] - centre[1])
            + centre[1])
    };
  }

  private static float[][] arrowHeadGenerator(final float[] point1, final float[] point2) {
    final float[] centreOfMass = new float[]{
        (point1[0] + point2[0]) * Graphics.scaleFactor / 2,
        (point1[1] + point2[1]) * Graphics.scaleFactor / 2
    };
    final float sideLength = 23.551f * Graphics.scaleFactor;
    float angle = (float) (Math.acos((point2[1] - point1[1]) / Math.sqrt(
        Math.pow(point2[0] - point1[0], 2) + Math.pow(point2[1] - point1[1], 2))));
    final float y2 = (float) (centreOfMass[1] - sideLength * Math.sin(Math.toRadians(60)) / 3f);
    final float[][] points = new float[][]{
        {centreOfMass[0],
            (float) (centreOfMass[1] + 2f * sideLength * Math.sin(Math.toRadians(60)) / 3f)},
        {centreOfMass[0] - sideLength / 2, y2},
        {centreOfMass[0] + sideLength / 2, y2}
    };
    if (point2[0] - point1[0] > 0) {
      angle *= -1;
    }
    for (int c = 0; c < points.length; c++) {
      points[c] = rotatePointAboutPoint(points[c], centreOfMass, angle);
    }
    return points;
  }

  public static void renderEdge(final float x1, final float y1, final float x2, final float y2,
      final ShapeRenderer shapeRenderer, final Boolean digraphStatus) {
    shapeRenderer.rectLine(x1 * Graphics.scaleFactor, y1 * Graphics.scaleFactor,
        x2 * Graphics.scaleFactor, y2 * Graphics.scaleFactor, 5 * Graphics.scaleFactor);
    if (digraphStatus) {
      final float[][] points = Graphics.arrowHeadGenerator(new float[]{x1, y1},
          new float[]{x2, y2});
      shapeRenderer.triangle(points[0][0], points[0][1], points[1][0], points[1][1], points[2][0],
          points[2][1]);
    }
  }

  public static void addTextToMenu(final Stage stage, final String title, final String[] attributes,
      final BitmapFont titleFont, final BitmapFont attributeFont) {
    final float[] black = new float[]{0, 0, 0, 1};
    stage.addActor(
        new Text(title, Gdx.graphics.getWidth() / 2f, 545 * Graphics.scaleFactor, titleFont, black,
            0, 0, -1));
    float y = 491.5f;
    for (String attribute : attributes) {
      stage.addActor(new Text(attribute,
          0.5f * (Gdx.graphics.getWidth() - 452 * Graphics.scaleFactor)
              + 16f * Graphics.scaleFactor, y * Graphics.scaleFactor, attributeFont, black, -1, 0,
          -1));
      y -= 61f;
    }
  }

  public static void drawMenu(final int numberOfAttributes, final ShapeRenderer shapeRenderer) {
    float width = 514 * Graphics.scaleFactor;
    float height = 424 * Graphics.scaleFactor;
    float x = (Gdx.graphics.getWidth() - width) / 2;
    float y = (Gdx.graphics.getHeight() - height) / 2;
    final float[] blue = new float[]{207f / 255f, 226f / 255f, 243f / 255f, 1};
    final float[] yellow = new float[]{1f, 229f / 255f, 153f / 255f, 1};
    Graphics.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * Graphics.scaleFactor,
        blue);
    if (numberOfAttributes > 0) {
      width = 452 * Graphics.scaleFactor;
      height = 46 * Graphics.scaleFactor;
      x = 0.5f * (Gdx.graphics.getWidth() - width); //old x was 414
      y = 468 * Graphics.scaleFactor;
      Graphics.drawRectangleWithBorder(shapeRenderer, x, y, width, height, 2 * Graphics.scaleFactor,
          yellow);
      for (int a = 1; a < numberOfAttributes; a++) {
        y -= 61 * Graphics.scaleFactor;
        Graphics.drawRectangleWithBorder(shapeRenderer, x, y, width, height,
            2 * Graphics.scaleFactor, yellow);
      }
    }
  }

  public static void setupBackAndApplyButtons(final TextButton back, final TextButton apply,
      final boolean visible) {
    back.setVisible(visible);
    back.setWidth(127 * Graphics.scaleFactor);
    back.setHeight(46 * Graphics.scaleFactor);
    back.setY(162 * Graphics.scaleFactor);
    back.setX(0.5f * (Gdx.graphics.getWidth() - 452 * Graphics.scaleFactor));
    apply.setVisible(visible);
    apply.setWidth(back.getWidth());
    apply.setHeight(back.getHeight());
    apply.setY(back.getY());
    apply.setX(back.getX() + 325 * Graphics.scaleFactor);
  }

  public static boolean mouseInBounds() {
    return Gdx.input.getX() / Graphics.scaleFactor > 160f && Gdx.input.getY() > 0
        && Gdx.input.getY() < Gdx.graphics.getHeight()
        && Gdx.input.getX() < Gdx.graphics.getWidth();
  }

  public static ArrayList<String> getItems(List<String> graphSelector) {
    final Object[] currentItems = graphSelector.getItems().items;
    final ArrayList<String> newItems = new ArrayList<>();
    for (final Object o : currentItems) {
      if (o != null) {
        newItems.add((String) o);
      }
    }
    return newItems;
  }

  public static void setupButtonBelow(final TextButton above, final TextButton toSetUp) {
    toSetUp.setWidth(127 * Graphics.scaleFactor);
    toSetUp.setHeight(46 * Graphics.scaleFactor);
    toSetUp.setPosition(above.getX(), above.getY() - 71 * Graphics.scaleFactor);
  }

  public static void setupButtonAbove(final TextButton below, final TextButton toSetUp) {
    toSetUp.setWidth(127 * Graphics.scaleFactor);
    toSetUp.setHeight(46 * Graphics.scaleFactor);
    toSetUp.setPosition(below.getX(), below.getY() + 71 * Graphics.scaleFactor);
  }

  public static void setupBottomButton(final TextButton button) {
    button.setWidth(127 * Graphics.scaleFactor);
    button.setHeight(46 * Graphics.scaleFactor);
    button.setPosition(80f * Graphics.scaleFactor - button.getWidth() / 2f,
        24f * Graphics.scaleFactor);
  }

  public static boolean applyNotPressed(final TextButton apply, final float x, final float y) {
    return !(x >= apply.getX()) || !(x <= apply.getX() + apply.getWidth()) || !(y >= apply.getY())
        || !(y <= apply.getY() + apply.getHeight());
  }
}