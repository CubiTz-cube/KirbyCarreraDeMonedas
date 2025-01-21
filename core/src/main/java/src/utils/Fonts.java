package src.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;
import src.utils.constants.MyColors;

public class Fonts implements Disposable {
    public final BitmapFont interFont;
    public final BitmapFont interNameFont;
    public final BitmapFont interNameFontSmall;
    public final BitmapFont briFont;
    public final BitmapFont briTitleFont;
    public final BitmapFont briBorderFont;

    public Fonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Bricolage_Grotesque/BricolageGrotesque_48pt-Regular.ttf"));
        briFont = FontCreator.createFont(48, Color.WHITE, generator, new FreeTypeFontGenerator.FreeTypeFontParameter());

        generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Inter/Inter_28pt-Regular.ttf"));
        interFont = FontCreator.createFont(40, Color.WHITE, generator, new FreeTypeFontGenerator.FreeTypeFontParameter());

        generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Bricolage_Grotesque/BricolageGrotesque_48pt-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderWidth = 4;
        parameter.borderColor = MyColors.BLUE;
        parameter.shadowColor = MyColors.BLUE;
        parameter.shadowOffsetX = -2;
        parameter.shadowOffsetY = 2;
        briTitleFont = FontCreator.createFont(48, MyColors.YELLOW, generator, parameter);
        parameter.borderColor = Color.BLACK;
        parameter.shadowColor = null;
        parameter.shadowOffsetX = 0;
        parameter.shadowOffsetY = 0;
        briBorderFont = FontCreator.createFont(48, Color.WHITE, generator, parameter);

        generator= new FreeTypeFontGenerator(Gdx.files.internal("ui/fonts/Inter/Inter_28pt-Regular.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderWidth = 3;
        parameter.borderColor = Color.BLACK;
        interNameFont = FontCreator.createFont(32, MyColors.PINK, generator, parameter);
        parameter.borderWidth = 2;
        interNameFontSmall = FontCreator.createFont(14, MyColors.PINK, generator, parameter);
    }


    @Override
    public void dispose() {
        interFont.dispose();
        interNameFont.dispose();
        interNameFontSmall.dispose();
        briFont.dispose();
        briTitleFont.dispose();
        briBorderFont.dispose();
    }
}
