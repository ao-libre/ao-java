package design.editors;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.jetbrains.annotations.NotNull;
import shared.model.npcs.NPC;

import static launcher.DesignCenter.SKIN;

public class NPCEditor extends Dialog {

    private NPC npc;

    public NPCEditor(NPC npc) {
        super("NPC Editor", SKIN);
        this.npc = npc;
        addTable();
        button("Cancel", false);
        button("OK", npc);
    }

    @NotNull
    public static Table getTable(NPC npc) {
        Table table = new Table(SKIN);
        table.setDebug(true);
        table.defaults().growX().uniform();
        // common
        table.add(IntegerEditor.create("ID", npc::setId, npc::getId)).row();
        table.add(StringEditor.simple("Name", npc::setName, npc::getName)).row();
        table.add(StringEditor.simple("Description", npc::setDesc, npc::getDesc)).row();
        table.add(IntegerEditor.create("Type", npc::setNpcType, npc::getNpcType)).row();

        // image
        table.add(IntegerEditor.create("Head", FieldProvider.HEAD, npc::setHead, npc::getHead)).row();
        table.add(IntegerEditor.create("Body", FieldProvider.BODY, npc::setBody, npc::getBody)).row();

        // hostile
        table.add(BooleanEditor.simple("Hostile", npc::setHostile, npc::isHostile)).row();
        table.add(IntegerEditor.create("Min HP", npc::setMinHP, npc::getMinHP)).row();
        table.add(IntegerEditor.create("Max HP", npc::setMaxHP, npc::getMaxHP)).row();
        table.add(IntegerEditor.create("Min Hit", npc::setMinHit, npc::getMinHit)).row();
        table.add(IntegerEditor.create("Max Hit", npc::setMaxHit, npc::getMaxHit)).row();
        table.add(IntegerEditor.create("Def", npc::setDef, npc::getDef)).row();
        table.add(IntegerEditor.create("Magic Def", npc::setDefM, npc::getDefM)).row();
        table.add(IntegerEditor.create("Evasion", npc::setEvasionPower, npc::getEvasionPower)).row();
        table.add(IntegerEditor.create("Attack", npc::setAttackPower, npc::getAttackPower)).row();
        table.add(IntegerEditor.create("Gold", npc::setGiveGLD, npc::getGiveGLD)).row();
        table.add(IntegerEditor.create("Exp", npc::setGiveEXP, npc::getGiveEXP)).row();
        npc.getSpells();
        npc.getSounds();
        npc.getExpressions();
        npc.getDrops();

        // trainer
        npc.getNpcSpanwer();

        // movement
        table.add(IntegerEditor.create("Movement" , npc::setMovement, npc::getMovement)).row();
        table.add(IntegerEditor.create("City", npc::setCity, npc::getCity)).row();
        table.add(IntegerEditor.create("Item Type", npc::setItemTypes, npc::getItemTypes)).row();

        // commerce
        npc.getObjs();

        return table;
    }

    private void addTable() {
        getContentTable().add(new ScrollPane(getTable(new NPC(npc)))).prefHeight(300).prefWidth(300);
    }

}