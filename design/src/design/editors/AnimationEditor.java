package design.editors;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import model.textures.AOAnimation;
import org.jetbrains.annotations.NotNull;

import static launcher.DesignCenter.SKIN;

public class AnimationEditor extends Dialog {

    private AOAnimation animation;

    public AnimationEditor(AOAnimation animation) {
        super("Animation Editor", SKIN);
        this.animation = animation;
        addTable();
        button("Cancel", false);
        button("OK", animation);
    }

    private void addTable() {
        getContentTable().add(new ScrollPane(getTable(animation))).prefHeight(300).prefWidth(300);
    }

    @NotNull
    public static Table getTable(AOAnimation animation) {
        Table table = new Table(SKIN);
        table.add(IntegerEditor.create("ID", id -> {
            animation.setId(id);
            // TODO refactor: search all items that use this animation to change it
        }, animation::getId)).expandX().row();
        table.add(FloatEditor.simple("Speed", animation::setSpeed, animation::getSpeed));
        int[] frames = animation.getFrames();
        for (int i = 0; i < frames.length; i++) {
            int finalI = i;
            table.add(IntegerEditor.create("Frame-" + i, FieldProvider.IMAGE, integer -> frames[finalI] = integer, () -> frames[finalI])).row();
        }

        return table;
    }

}