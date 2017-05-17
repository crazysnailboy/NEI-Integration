package tonius.neiintegration.mods.masterchef;

import java.util.ArrayList;
import java.util.List;

import com.chef.mod.crafting.BoilerRecipes;
import com.chef.mod.gui.GuiBoiler;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;

public class RecipeHandlerBoiler extends RecipeHandlerBase {

    private static final List<BoilerRecipe> cachedRecipes = new ArrayList<BoilerRecipe>();

    public class BoilerRecipe extends tonius.neiintegration.RecipeHandlerBase.CachedBaseRecipe {
        private PositionedStack ingredient;
        private List<PositionedStack> otherStacks = new ArrayList<PositionedStack>();
        private PositionedStack result;

        public BoilerRecipe(Item ingredient, ItemStack result) {
            this.ingredient = new PositionedStack(new ItemStack(ingredient), 65, 36);
            this.result = new PositionedStack(result, 133, 36);
            this.otherStacks.add(new PositionedStack(new ItemStack(Items.coal), 12, 49));
            this.otherStacks.add(new PositionedStack(new ItemStack(Items.water_bucket), 38, 9));
        }

        @Override
        public PositionedStack getIngredient() {
            return this.ingredient;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.otherStacks;
        }

        @Override
        public PositionedStack getResult() {
            return this.result;
        }

    }

    @Override
    public String getRecipeID() {
        return "chef.sauceMaker";
    }

    @Override
    public String getRecipeName() {
        return Utils.translate("container.boiler", false);
    }

    @Override
    public String getGuiTexture() {
        return "chef:textures/gui/container/boiler.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiBoiler.class;
    }

    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(8, 0, 13, 15, 146, 66); // background
        GuiDraw.drawTexturedModalRect(11, 4, 176, 14, 21, 25); // water
        GuiDraw.drawTexturedModalRect(12, 33, 176, 0, 14, 14); // fire
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(12, 33, 17, 49, 14, 14, 100, 1); // fire
        this.drawProgressBar(93, 29, 176, 55, 24, 4, 50, 0); // bubbles
        this.drawProgressBar(93, 35, 176, 39, 24, 16, 100, 0); // arrow
    }

    private List<BoilerRecipe> getCachedRecipes() {
        if (cachedRecipes.size() == 0) {
            for (Object key : Item.itemRegistry.getKeys()) {
                Item item = (Item) Item.itemRegistry.getObject(key);
                ItemStack result = BoilerRecipes.getBoilingResult(item);
                if (result != null) {
                    cachedRecipes.add(new BoilerRecipe(item, result));
                }
            }
        }
        return cachedRecipes;
    }

    @Override
    public void loadAllRecipes() {
        this.arecipes.addAll(this.getCachedRecipes());
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (BoilerRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getResult().item, result)) {
                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        for (BoilerRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getIngredient().item, ingred)) {
                this.arecipes.add(recipe);
            }
        }
    }

}
