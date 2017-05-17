package tonius.neiintegration.mods.masterchef;

import java.util.ArrayList;
import java.util.List;

import com.chef.mod.crafting.SauceMakerRecipes;
import com.chef.mod.gui.GuiSauceMaker;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;

public class RecipeHandlerSauceMaker extends RecipeHandlerBase {

    private static final List<SauceMakerRecipe> cachedRecipes = new ArrayList<SauceMakerRecipe>();

    public class SauceMakerRecipe extends tonius.neiintegration.RecipeHandlerBase.CachedBaseRecipe {
        private PositionedStack ingredient;
        private List<PositionedStack> otherStacks = new ArrayList<PositionedStack>();
        private PositionedStack result;

        public SauceMakerRecipe(Item ingredient, ItemStack result) {
            this.ingredient = new PositionedStack(new ItemStack(ingredient), 16, 2);
            this.result = new PositionedStack(result, 115, 22);
            this.otherStacks.add(new PositionedStack(new ItemStack(Items.coal), 16, 44));
            this.otherStacks.add(new PositionedStack(new ItemStack(Items.bowl), 44, 44));
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
        return Utils.translate("container.sauceMaker", false);
    }

    @Override
    public String getGuiTexture() {
        return "chef:textures/gui/container/sauceMaker.png";
    }

    @Override
    public void loadTransferRects() {
        this.addTransferRect(76, 27, 14, 12);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiSauceMaker.class;
    }

    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(2, 0, 7, 7, 162, 62); // background
        GuiDraw.drawTexturedModalRect(18, 20, 176, 0, 12, 22); // bubbles
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(18, 20, 23, 27, 12, 22, 100, 1); // bubbles
        this.drawProgressBar(40, 23, 176, 22, 68, 13, 100, 0); // progress
    }

    private List<SauceMakerRecipe> getCachedRecipes() {
        if (cachedRecipes.size() == 0) {
            for (Object key : Item.itemRegistry.getKeys()) {
                Item item = (Item) Item.itemRegistry.getObject(key);
                ItemStack result = SauceMakerRecipes.getJuicingResult(item, Items.bowl);
                if (result != null) {
                    cachedRecipes.add(new SauceMakerRecipe(item, result));
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
        for (SauceMakerRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getResult().item, result)) {
                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        System.out.println("loadUsageRecipes :: " + ingred == null ? "null" : ingred.getItem().getUnlocalizedName());
        for (SauceMakerRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getIngredient().item, ingred)) {
                this.arecipes.add(recipe);
            }
        }
    }

}
