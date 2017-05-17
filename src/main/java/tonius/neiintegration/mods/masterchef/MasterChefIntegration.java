package tonius.neiintegration.mods.masterchef;

import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.Utils;

public class MasterChefIntegration extends IntegrationBase {

    @Override
    public String getName() {
        return "Master Chef";
    }

    @Override
    public boolean isValid() {
        return Utils.isModLoaded("chef");
    }

    @Override
    public void loadConfig() {
        this.registerHandler(new RecipeHandlerBoiler());
        this.registerHandler(new RecipeHandlerCookingFurnace());
        this.registerHandler(new RecipeHandlerDehydrator());
        this.registerHandler(new RecipeHandlerIceCreamMaker());
        this.registerHandler(new RecipeHandlerSauceMaker());
        this.registerHandler(new RecipeHandlerWaffleMaker());
    }

}
