package org.zycong.theHordes.helpers.Traders;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.zycong.theHordes.commands.CommandRegister.CommandRegister;
import org.zycong.theHordes.helpers.GUI.GUI.GUI;

import java.util.List;

@CommandRegister.AutoRegisterer
public class ShopKeeper {

    private final List<Integer> ShopBorderSlot = List.of(27, 28, 29, 30, 31, 32, 33, 34, 35);

    private final List<Integer> ShopIngredient1Slot = List.of(18, 19, 20, 21, 22, 23, 24, 25, 26);
    private final List<Integer> ShopIngredient2Slot = List.of(9, 10, 11, 12, 13, 14, 15, 16, 17);
    private final List<Integer> ShopResultSlot = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8);

    @CommandRegister.Command(
            name = "shop",
            playerOnly = true,
            permission = "theHordes.shop",
            aliases = {"shopkeeper", "skeeper", "shopk", "sk"},
            description = "A Command to create shopkeeper",
            args = {
                    @CommandRegister.Arguments(
                            args = {
                                    @CommandRegister.Arg(arg = "create", permission = "theHordes.shop.create"),
                                    @CommandRegister.Arg(arg = "noAI", permission = "theHordes.item.noAI"),
                                    @CommandRegister.Arg(arg = "baby", permission = "theHordes.item.baby"),
                                    @CommandRegister.Arg(arg = "profession", permission = "theHordes.item.profession")
                            }
                    )
            },
            root = "WhereWaldosUtils"
    )
    void shop(CommandSender commandSender, String[] args){
        switch (args[1]){
            case "create": {
                Player sender = (Player) commandSender;
            }
        }
    }

    private MerchantRecipe CreateTrade(ItemStack ingredient1, ItemStack ingredient2, ItemStack output){
        MerchantRecipe merchantRecipe = new MerchantRecipe(output, Integer.MAX_VALUE);
        merchantRecipe.addIngredient(ingredient1);
        merchantRecipe.addIngredient(ingredient2);
        merchantRecipe.setIgnoreDiscounts(true);
        merchantRecipe.setPriceMultiplier(0);
        merchantRecipe.setUses(0);
        return merchantRecipe;
    }

    private Merchant CreateTrader(List<MerchantRecipe> trades, TextComponent Title){
        Merchant merchant = Bukkit.createMerchant(Title);
        merchant.setRecipes(trades);
        return merchant;
    }

    private void OpenMerchant(Player p, Merchant merchant){
        p.openMerchant(merchant, true);
    }
}
