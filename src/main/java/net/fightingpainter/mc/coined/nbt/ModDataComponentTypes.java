package net.fightingpainter.mc.coined.nbt;

import java.util.function.UnaryOperator;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.util.Money;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES_REGISTER = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Coined.MOD_ID);
    
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Money>> MONEY = registerDataComponentType("money", builder -> builder.persistent(Money.CODEC));
    

    //register data component type (idk)
    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerDataComponentType(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES_REGISTER.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }
    

    //==================================================================================\\
    /**
     * Register Data Component Types
     * @param modEventBus The mod event bus to register the data component types to
    */
    public static void register(IEventBus modEventBus) {
        Coined.LOGGER.info("Registering DataComponents (Idk I'm scared I just want to save nbt data twt)...");
        DATA_COMPONENT_TYPES_REGISTER.register(modEventBus); //Register the data component types
    }
    
}
