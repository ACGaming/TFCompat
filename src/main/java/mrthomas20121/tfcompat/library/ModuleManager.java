package mrthomas20121.tfcompat.library;

import mrthomas20121.tfcompat.TFCompat;
import mrthomas20121.tfcompat.compat.actuallyadditions.ActuallyAdditionsModule;
import mrthomas20121.tfcompat.compat.betterwithmods.BetterWithModsModule;
import mrthomas20121.tfcompat.compat.forestry.ForestryModule;
import mrthomas20121.tfcompat.compat.mekanism.MekanismModule;
import mrthomas20121.tfcompat.compat.pyrotech.PyrotechModule;
import mrthomas20121.tfcompat.compat.thaumcraft.ThaumcraftModule;
import mrthomas20121.tfcompat.compat.thermalexpansion.ThermalExpansionModule;
import mrthomas20121.tfcompat.library.recipes.IHeatRecipe;
import mrthomas20121.tfcompat.library.recipes.IRecipeRemoval;
import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.dries007.tfc.api.recipes.knapping.KnappingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = TFCompat.MODID)
public class ModuleManager
{

    private static ArrayList<ModuleCore> modules = new ArrayList<>();

    public static void registerModule(ModuleCore module)
    {
        if(Loader.isModLoaded(module.getDep()))
        {
            modules.add(module);
        }
    }

    public static void registerModules(ModuleCore... mods)
    {
        for(ModuleCore module : mods)
        {
            registerModule(module);
        }
    }

    public static ArrayList<ModuleCore> getModules() {
        return modules;
    }

    public static void initModules()
    {
        registerModules(
                new ActuallyAdditionsModule(),
                new BetterWithModsModule(),
                new ForestryModule(),
                new MekanismModule(),
                new PyrotechModule(),
                new ThaumcraftModule(),
                new ThermalExpansionModule()
        );
    }

    @SubscribeEvent
    public static void onRegisterRecipesEvent(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistry<IRecipe> r = event.getRegistry();

        for(ModuleCore module : modules)
        {
                module.initRecipes(r);
                if(module instanceof IRecipeRemoval)
                {
                    ((IRecipeRemoval)module).removal(r);
                }
        }
    }

    @SubscribeEvent
    public static void onRegisterHeatRecipeEvent(RegistryEvent.Register<HeatRecipe> event)
    {
        for(ModuleCore module : modules)
        {
            if(module instanceof IHeatRecipe)
            {
                ((IHeatRecipe) module).initHeatRecipes(event.getRegistry());
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterKnappingRecipeEvent(RegistryEvent.Register<KnappingRecipe> event)
    {
    }
}
