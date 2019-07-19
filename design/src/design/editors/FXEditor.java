package design.editors;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import model.descriptors.Descriptor;
import model.descriptors.FXDescriptor;
import org.jetbrains.annotations.NotNull;

import static launcher.DesignCenter.SKIN;

public class FXEditor extends DescriptorEditor {

    public FXEditor(Descriptor descriptor) {
        super(descriptor);
    }

    public static Table create(FXDescriptor descriptor) {
        return new FXEditor(descriptor).getTable(descriptor);
    }

    @NotNull
    @Override
    public Table getTable(Descriptor descriptor) {
        Table table = new Table(SKIN);
        table.setDebug(true);
        table.defaults().growX().uniform();

        table.add(IntegerEditor.create("ID", descriptor::setId, descriptor::getId)).row();

        getExtraFields(descriptor, table);

        table.add(IntegerEditor.create("Animation", FieldProvider.ANIMATION, b -> descriptor.getIndexs()[0] = b, () -> descriptor.getGraphic(0))).row();

        return table;
    }

    @Override
    protected void getExtraFields(Descriptor descriptor, Table table) {
        FXDescriptor fxDescriptor = (FXDescriptor) descriptor;
        table.add(IntegerEditor.create("Offset X", fxDescriptor::setOffsetX, fxDescriptor::getOffsetX)).row();
        table.add(IntegerEditor.create("Offset Y", fxDescriptor::setOffsetY, fxDescriptor::getOffsetY)).row();
    }
}