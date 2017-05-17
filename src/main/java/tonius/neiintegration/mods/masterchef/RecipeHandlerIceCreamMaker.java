package tonius.neiintegration.mods.masterchef;

import java.util.ArrayList;
import java.util.List;

import com.chef.mod.crafting.IceCreamMakerRecipes;
import com.chef.mod.gui.GuiIceCreamMaker;
import com.chef.mod.init.MyItems;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;

public class RecipeHandlerIceCreamMaker extends RecipeHandlerBase {

    private static final List<IceCreamMakerRecipe> cachedRecipes = new ArrayList<IceCreamMakerRecipe>();

    public class IceCreamMakerRecipe extends tonius.neiintegration.RecipeHandlerBase.CachedBaseRecipe {
        private PositionedStack ingredient;
        private List<PositionedStack> otherStacks = new ArrayList<PositionedStack>();
        private PositionedStack result;

        public IceCreamMakerRecipe(Item ingredient, ItemStack result) {
            this.ingredient = new PositionedStack(new ItemStack(ingredient), 40, 33);
            this.result = new PositionedStack(result, 115, 18);
            this.otherStacks.add(new PositionedStack(new ItemStack(MyItems.ice_shard), 3, 54));
            this.otherStacks.add(new PositionedStack(new ItemStack(Items.sugar), 40, 2));
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
        return "chef.iceCreamMaker";
    }

    @Override
    public String getRecipeName() {
        return Utils.translate("container.iceCreamMaker", false);
    }

    @Override
    public String getGuiTexture() {
        return "chef:textures/gui/container/iceCreamMaker.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiIceCreamMaker.class;
    }

    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(1, 0, 6, 9, 136, 72); // background
        GuiDraw.drawTexturedModalRect(3, 2, 176, 43, 16, 47); // ice
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(59, 4, 176, 0, 51, 43, 100, 0); // arrow
    }

    private List<IceCreamMakerRecipe> getCachedRecipes() {
        if (cachedRecipes.size() == 0) {
            for (Object key : Item.itemRegistry.getKeys()) {
                Item item = (Item) Item.itemRegistry.getObject(key);
                ItemStack result = IceCreamMakerRecipes.getIceCreamResult(item, Items.sugar);
                if (result != null) {
                    cachedRecipes.add(new IceCreamMakerRecipe(item, result));
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
        for (IceCreamMakerRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getResult().item, result)) {
                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        for (IceCreamMakerRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getIngredient().item, ingred)) {
                this.arecipes.add(recipe);
            }
        }
    }

}
