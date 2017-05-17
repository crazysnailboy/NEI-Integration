package tonius.neiintegration.mods.masterchef;

import java.util.ArrayList;
import java.util.List;

import com.chef.mod.crafting.CookingFurnaceRecipes;
import com.chef.mod.gui.GuiCookingFurnace;
import com.chef.mod.init.MyItems;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;

public class RecipeHandlerCookingFurnace extends RecipeHandlerBase {

    private static final List<CookingFurnaceRecipe> cachedRecipes = new ArrayList<CookingFurnaceRecipe>();

    public class CookingFurnaceRecipe extends tonius.neiintegration.RecipeHandlerBase.CachedBaseRecipe {
        private List<PositionedStack> ingredients = new ArrayList<PositionedStack>();
        private List<PositionedStack> otherStacks = new ArrayList<PositionedStack>();
        private PositionedStack result;

        public CookingFurnaceRecipe(Item ingredient, ItemStack result, boolean requiresButter) {
            this(new ItemStack(ingredient), null, result, requiresButter);
        }

        public CookingFurnaceRecipe(ItemStack ingredient1, ItemStack ingredient2, ItemStack result, boolean requiresButter) {
            this.ingredients.add(new PositionedStack(ingredient1, 27, 1));
            if (ingredient2 != null) {
                this.ingredients.add(new PositionedStack(ingredient2, 51, 1));
            }
            this.otherStacks.add(new PositionedStack(new ItemStack(Items.coal), 51, 35));
            if (requiresButter) {
                this.otherStacks.add(new PositionedStack(new ItemStack(MyItems.butter), 27, 35));
            }
            this.result = new PositionedStack(result, 111, 17);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return this.ingredients;
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
        return "chef.cookingFurnace";
    }

    @Override
    public String getRecipeName() {
        return Utils.translate("container.cookingFurnace", false);
    }

    @Override
    public String getGuiTexture() {
        return "chef:textures/gui/container/cookingFurnace.png";
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCookingFurnace.class;
    }

    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(16, 0, 21, 18, 139, 52); // background
        GuiDraw.drawTexturedModalRect(51, 19, 176, 0, 14, 14); // fire
        GuiDraw.drawTexturedModalRect(27, 20, 176, 31, 16, 12); // butter
        GuiDraw.drawTexturedModalRect(16, 19, 176, 56, 9, 13); // oil bottle
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(74, 17, 176, 14, 24, 16, 100, 0); // arrow
        this.drawProgressBar(51, 19, 56, 37, 14, 14, 100, 1); // fire
    }

    private List<CookingFurnaceRecipe> getCachedRecipes() {
        if (cachedRecipes.size() == 0) {
            cachedRecipes.add(new CookingFurnaceRecipe(new ItemStack(Items.dye, 1, 3), null, new ItemStack(MyItems.dark_chocolate), false));

            for (Object key : Item.itemRegistry.getKeys()) {
                Item item = (Item) Item.itemRegistry.getObject(key);

                ItemStack result1 = CookingFurnaceRecipes.getSingleItemCookingResult(item);
                if (result1 != null) {
                    cachedRecipes.add(new CookingFurnaceRecipe(item, result1, CookingFurnaceRecipes.requiresButter(item)));
                }
                ItemStack result2 = CookingFurnaceRecipes.getDoubleItemCookingResult(MyItems.bread_crumbs, item);
                if (result2 != null) {
                    cachedRecipes.add(new CookingFurnaceRecipe(new ItemStack(MyItems.bread_crumbs), new ItemStack(item), result2, CookingFurnaceRecipes.requiresButter(item)));
                }
            }

            for (Object key : Block.blockRegistry.getKeys()) {
                Block block = (Block) Block.blockRegistry.getObject(key);
                Item item = Item.getItemFromBlock(block);

                ItemStack result1 = CookingFurnaceRecipes.getSingleItemCookingResult(item);
                if (result1 != null) {
                    cachedRecipes.add(new CookingFurnaceRecipe(item, result1, CookingFurnaceRecipes.requiresButter(item)));
                }
                ItemStack result2 = CookingFurnaceRecipes.getDoubleItemCookingResult(MyItems.bread_crumbs, item);
                if (result2 != null) {
                    cachedRecipes.add(new CookingFurnaceRecipe(new ItemStack(MyItems.bread_crumbs), new ItemStack(item), result2, CookingFurnaceRecipes.requiresButter(item)));
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
        for (CookingFurnaceRecipe recipe : this.getCachedRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getResult().item, result)) {
                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        for (CookingFurnaceRecipe recipe : this.getCachedRecipes()) {
            for ( PositionedStack stack : recipe.getIngredients() ) {
                if (Utils.areStacksSameTypeCraftingSafe(stack.item, ingred)) {
                    this.arecipes.add(recipe);
                }
            }
        }
    }

}
