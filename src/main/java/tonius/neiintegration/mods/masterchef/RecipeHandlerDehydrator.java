package tonius.neiintegration.mods.masterchef;

import java.util.ArrayList;
import java.util.List;

import com.chef.mod.crafting.DehydratorRecipes;
import com.chef.mod.gui.GuiDehydrator;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;

public class RecipeHandlerDehydrator extends RecipeHandlerBase {

    private static final List<DehydratorRecipe> cachedRecipes = new ArrayList<DehydratorRecipe>();

    public class DehydratorRecipe extends tonius.neiintegration.RecipeHandlerBase.CachedBaseRecipe {
        private PositionedStack ingredient;
        private List<PositionedStack> otherStacks = new ArrayList<PositionedStack>();
        private PositionedStack result;

        public DehydratorRecipe(Item ingredient, ItemStack result) {
            this.ingredient = new PositionedStack(new ItemStack(ingredient), 31, 59);
            this.result = new PositionedStack(result, 31, 2);

            this.otherStacks.add(new PositionedStack(new ItemStack(ingredient), 59, 59));
            this.otherStacks.add(new PositionedStack(new ItemStack(ingredient), 87, 59));
            this.otherStacks.add(new PositionedStack(new ItemStack(ingredient), 115, 59));

            this.otherStacks.add(new PositionedStack(result, 59, 2));
            this.otherStacks.add(new PositionedStack(result, 87, 2));
            this.otherStacks.add(new PositionedStack(result, 115, 2));

            this.otherStacks.add(new PositionedStack(new ItemStack(Items.coal), 143, 30));
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
        return "chef.dehydrator";
    }

    @Override
    public String getRecipeName() {
        return Utils.translate("container.dehydrator", false);
    }

    @Override
    public String getGuiTexture() {
        return "chef:textures/gui/container/dehydrator.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiDehydrator.class;
    }

    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(29, 0, 34, 3, 132, 77); // background
        GuiDraw.drawTexturedModalRect(143, 14, 176, 0, 14, 14); // fire
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(143, 14, 148, 18, 14, 14, 100, 1); // fire
        this.drawProgressBar(33, 24, 176, 14, 12, 29, 200, 3); // steam 1
        this.drawProgressBar(61, 24, 176, 14, 12, 29, 200, 3); // steam 2
        this.drawProgressBar(89, 24, 176, 14, 12, 29, 200, 3); // steam 3
        this.drawProgressBar(117, 24, 176, 14, 12, 29, 200, 3); // steam 4
    }

    private List<DehydratorRecipe> getCachedRecipes() {
        if (cachedRecipes.size() == 0) {
            for (Object key : Item.itemRegistry.getKeys()) {
                Item item = (Item) Item.itemRegistry.getObject(key);
                ItemStack result = DehydratorRecipes.getDryingResult(item);
                if (result != null) {
                    cachedRecipes.add(new DehydratorRecipe(item, result));
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
        for (DehydratorRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getResult().item, result)) {
                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        for (DehydratorRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getIngredient().item, ingred)) {
                this.arecipes.add(recipe);
            }
        }
    }

}
