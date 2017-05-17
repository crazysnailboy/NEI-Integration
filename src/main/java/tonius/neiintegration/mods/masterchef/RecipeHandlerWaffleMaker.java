package tonius.neiintegration.mods.masterchef;

import java.util.ArrayList;
import java.util.List;

import com.chef.mod.crafting.WaffleMakerRecipes;
import com.chef.mod.gui.GuiWaffleMaker;
import com.chef.mod.init.MyItems;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;

public class RecipeHandlerWaffleMaker extends RecipeHandlerBase {

    private static final List<WaffleMakerRecipe> cachedRecipes = new ArrayList<WaffleMakerRecipe>();

    public class WaffleMakerRecipe extends tonius.neiintegration.RecipeHandlerBase.CachedBaseRecipe {
        private PositionedStack ingredient;
        private List<PositionedStack> otherStacks = new ArrayList<PositionedStack>();
        private PositionedStack result;

        public WaffleMakerRecipe(Item ingredient, ItemStack result) {
            this.ingredient = new PositionedStack(new ItemStack(ingredient), 61, 1);
            this.result = new PositionedStack(result, 39, 29);

            this.otherStacks.add(new PositionedStack(new ItemStack(ingredient), 89, 1));
            this.otherStacks.add(new PositionedStack(result, 111, 29));

            this.otherStacks.add(new PositionedStack(new ItemStack(Items.coal), 5, 51));
            this.otherStacks.add(new PositionedStack(new ItemStack(MyItems.olive_oil), 145, 51));

            this.otherStacks.add(new PositionedStack(new ItemStack(Items.bowl), 39, 54));
            this.otherStacks.add(new PositionedStack(new ItemStack(Items.bowl), 111, 54));

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
        return "chef.waffleMaker";
    }

    @Override
    public String getRecipeName() {
        return Utils.translate("container.waffleMaker", false);
    }

    @Override
    public String getGuiTexture() {
        return "chef:textures/gui/container/waffleMaker.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiWaffleMaker.class;
    }

    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(4, 0, 9, 4, 158, 74); // background
        GuiDraw.drawTexturedModalRect(5, 33, 176, 0, 14, 14); // fire
        GuiDraw.drawTexturedModalRect(134, 35, 176, 110, 9, 13); // oil bottle
        GuiDraw.drawTexturedModalRect(145, 36, 176, 85, 16, 12); // butter
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(5, 33, 10, 37, 14, 14, 100, 1); // fire
    }

    private List<WaffleMakerRecipe> getCachedRecipes() {
        if (cachedRecipes.size() == 0) {
            for (Object key : Item.itemRegistry.getKeys()) {
                Item item = (Item) Item.itemRegistry.getObject(key);
                ItemStack result = WaffleMakerRecipes.getBakingResult(item);
                if (result != null) {
                    cachedRecipes.add(new WaffleMakerRecipe(item, result));
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
        for (WaffleMakerRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getResult().item, result)) {
                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        for (WaffleMakerRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getIngredient().item, ingred)) {
                this.arecipes.add(recipe);
            }
        }
    }

}
