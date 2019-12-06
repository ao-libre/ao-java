package game.ui;


import com.artemis.E;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.minlog.Log;
import entity.character.info.Inventory;
import game.AOGame;
import game.handlers.AOAssetManager;
import game.handlers.ObjectHandler;
import game.screens.GameScreen;
import game.utils.Skins;
import shared.network.interaction.AddItem;
import shared.objects.types.Obj;
import shared.objects.types.WorkKind;
import shared.util.Messages;


public class WorkUI extends Table {

    private final ClickListener mouseListener;
    private AOAssetManager assetManager;
    private int needObjID = 0, needCount = 0, needCount2 = 0, needObjID2 = 0, needCount3 = 0, needObjID3 = 0, resultObjID = 0 , resultCount = 0;
    private SelectBox<SawRecipes> sawRecipesSelect;
    private SelectBox<ForgeRecipes> forgeRecipesSelect;

    public WorkUI() {
        super( Skins.COMODORE_SKIN );
        setBackground( "simple-window" );
        this.mouseListener = new ClickListener();
        this.assetManager = AOGame.getGlobalAssetManager();
        add( sawWork() ).prefSize( 400, 400 ).row();
    }
    //cambia  el contenido de la tabla segun el tipo de trabajo
    public void notify(WorkKind workKind){
        switch(workKind){
            case SAW:
                clear();
                setSkin( Skins.COMODORE_SKIN );
                setBackground( "simple-window");
                add(sawWork()).prefSize( 400,400 ).row();
                break;
            case FORGE:
                clear();
                setSkin( Skins.COMODORE_SKIN );
                setBackground( "simple-window" );
                add(forgeWork()).prefSize( 400,400 ).row();
                break;
        }
    }
    //crea la UI de herreria
    private Table forgeWork() {
        Table forgeTable = new Table(  );
        forgeRecipesSelect = new SelectBox<>(getSkin());
        final Array<ForgeRecipes> recipes = new Array<>();
        ForgeRecipes.getForgeRecipes().forEach(recipes::add);
        forgeRecipesSelect.setItems(recipes);
        forgeRecipesSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                forgeTable.clearChildren();
                forgeTable.add( forgeRecipesSelect ).top().row();
                ForgeRecipes forgeRecipes = forgeRecipesSelect.getSelected();
                //obtiene ingredientes
                needCount = forgeRecipes.getNeedCount();
                needObjID = forgeRecipes.getNeedObjID();
                needCount2 = forgeRecipes.getNeedCount2();
                needObjID2 = forgeRecipes.getNeedObjID2();
                needCount3 = forgeRecipes.getNeedCount3();
                needObjID3 = forgeRecipes.getNeedObjID3();
                //obtiene resultado
                resultCount = forgeRecipes.getResultCount();
                resultObjID = forgeRecipes.getResultObjID();

                forgeTable.add(createContent( needCount, needObjID, needCount2, needObjID2, needCount3, needObjID3, resultObjID, resultCount  ) )
                        .prefSize( 400,400 ).top().row();
                forgeTable.add( createButton() ).bottom();
            }
        });
        forgeTable.add( forgeRecipesSelect ).row();
        //todo crear recetas y ver como obtener los campos need y result
        forgeTable.add( createContent( needCount, needObjID, needCount2, needObjID2, needCount3, needObjID3, resultObjID, resultCount  ) )
                .prefSize( 400,400 ).top().row();
        forgeTable.add( createButton() ).bottom();
        return forgeTable;
    }
    //crea la UI de carpinteria
    private Table sawWork() {
        Table sawTable = new Table( Skins.COMODORE_SKIN );

        sawRecipesSelect = new SelectBox<>(getSkin());
        final Array<SawRecipes> recipes = new Array<>();
        SawRecipes.getSawRecipes().forEach(recipes::add);
        sawRecipesSelect.setItems(recipes);
        sawRecipesSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sawTable.clearChildren();
                sawTable.add( sawRecipesSelect ).top().row();
                SawRecipes sawRecipes = sawRecipesSelect.getSelected();
                //obtiene ingredientes
                needCount = sawRecipes.getNeedCount();
                needObjID = sawRecipes.getNeedObjID();
                needCount2 = sawRecipes.getNeedCount2();
                needObjID2 = sawRecipes.getNeedObjID2();
                needCount3 = sawRecipes.getNeedCount3();
                needObjID3 = sawRecipes.getNeedObjID3();
                //obtiene resultado
                resultCount = sawRecipes.getResultCount();
                resultObjID = sawRecipes.getResultObjID();

                sawTable.add(createContent(  needCount, needObjID, needCount2, needObjID2, needCount3, needObjID3, resultObjID, resultCount  ) )
                        .prefSize( 400,400 ).top().row();
                sawTable.add( createButton() ).bottom();
            }
        });
        sawTable.add( sawRecipesSelect ).row();
        sawTable.add(createContent( needCount, needObjID, needCount2, needObjID2, needCount3, needObjID3, resultObjID, resultCount ) )
                .prefSize( 400,400 ).top().row();
        sawTable.add( createButton() ).bottom();
        return sawTable;
    }
    //crea el contedio central del UI
    private Table createContent(int needCount, int needObjID, int needCount2, int needObjID2 ,int needCount3, int needObjID3, int resultObjID, int resultCount){

        Table content = new Table( Skins.COMODORE_SKIN );

        Label labelNeed = new Label("Recursos requeridos: ", Skins.COMODORE_SKIN  );
        //objetos requeridos 1
        Label labelNeedObj = new Label( " ", Skins.COMODORE_SKIN );
        if (needObjID > 0) {
            Obj needObj = GameScreen.world.getSystem( ObjectHandler.class ).getObject( needObjID ).get();
            labelNeedObj = new Label( needCount + " " + needObj.getName(), Skins.COMODORE_SKIN );
        }
        //objetos requeridos 2
        Label labelNeedObj2 = new Label( " ", Skins.COMODORE_SKIN );
        if (needObjID2 > 0) {
            Obj needObj2 = GameScreen.world.getSystem( ObjectHandler.class ).getObject( needObjID2 ).get();
            labelNeedObj2 = new Label( needCount2 + " " + needObj2.getName(), Skins.COMODORE_SKIN );
        }
        //objetos requeridos 3
        Label labelNeedObj3 = new Label( " ", Skins.COMODORE_SKIN );
        if (needObjID3 > 0) {
            Obj needObj3 = GameScreen.world.getSystem( ObjectHandler.class ).getObject( needObjID3 ).get();
            labelNeedObj3 = new Label( needCount3 + " " + needObj3.getName(), Skins.COMODORE_SKIN );
        }

        Label labelResult = new Label( "Resultado: ", Skins.COMODORE_SKIN );
        //objeto resultante
        Label labelResultObj = new Label( "", Skins.COMODORE_SKIN );
        if (resultObjID > 0) {
            Obj resultObj = GameScreen.world.getSystem( ObjectHandler.class ).getObject( resultObjID ).get();
            labelResultObj = new Label( resultCount + " " + resultObj.getName(), Skins.COMODORE_SKIN );
        }

        content.add(" ").top().row();
        content.add( labelNeed ).center().row();
        content.add( labelNeedObj ).center().row();
        content.add( labelNeedObj2 ).center().row();
        content.add( labelNeedObj3 ).center().row();
        content.add(" ").row();
        content.add( labelResult ).center().row();
        content.add( labelResultObj ).center().row();
        return content;
    }
    //crea el boton craft y la funcionalidad
    private Button createButton(){
        TextButton create = new TextButton( "Craft", getSkin() );
        create.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(hasRequestItems(needCount, needObjID ,needCount2, needObjID2, needCount3, needObjID3 )) {
                    //remueve los items necesarios
                    GameScreen.getClient().sendToAll( new AddItem( E.E( GameScreen.getPlayer() )
                            .getNetwork().id, needObjID,  -needCount ) );
                    if (needObjID2>0){
                        GameScreen.getClient().sendToAll( new AddItem( E.E( GameScreen.getPlayer() )
                                .getNetwork().id, needObjID2,  -needCount2 ) );
                    }
                    if (needObjID3>0){
                        GameScreen.getClient().sendToAll( new AddItem( E.E( GameScreen.getPlayer() )
                                .getNetwork().id, needObjID3,  -needCount3 ) );
                    }
                    //agrega el resultado de la creacion
                    GameScreen.getClient().sendToAll( new AddItem( E.E( GameScreen.getPlayer() )
                            .getNetwork().id, resultObjID, resultCount ) );
                } else {
                    GameScreen.world.getSystem( GUI.class ).getConsole().addInfo(assetManager.getMessages(
                            Messages.ADD_OR_REMOVE_ITEMS, "No tienes los recursos necesarios" ));
                }
            }
        } );
        return create;
    }
    //cambia  el tipo de trabajo
    public void setWorkKind(WorkKind workKind){
        notify( workKind );
    }

    //chequea que poseas los items necesarios para la creacion
    private boolean hasRequestItems(int needCount, int needObjID, int needCount2, int needObjID2,int needCount3, int needObjID3){
        E player = E.E (GameScreen.getPlayer());
        Inventory.Item[] items = player.getInventory().items;
        boolean need1=false, need2=false, need3=false;

        if (needObjID2 == 0){
            need2 = true;
        }
        if (needObjID3 == 0){
            need3 = true;
        }

        for (int i = 0; i <20 ; i++) {
            if (items[i] != null){
                if (!need1) {
                    if(items[i].objId == needObjID) {
                        if(items[i].count >= needCount) {
                            need1 = true;
                        }
                    }
                }
                if (!need2) {
                    if(items[i].objId == needObjID2) {
                        if(items[i].count >= needCount2) {
                            need2 = true;
                        }
                    }
                }
                if(!need3) {
                    if(items[i].objId == needObjID3) {
                        if(items[i].count >= needCount3) {
                            need3 = true;
                        }
                    }
                }
            }

        }

        if (need1 && need2 && need3){
            return true;
        } else {
            return false;
        }
    }

    public void isOver(){
        mouseListener.isOver();
    }
}
