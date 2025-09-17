package net.fightingpainter.mc.coined.data;

import java.util.function.UnaryOperator;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fightingpainter.mc.coined.Coined;
import net.fightingpainter.mc.coined.util.Money;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModDataTypes {
    //======================================== Registrars ========================================\\
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES_REGISTER = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Coined.MOD_ID); //the register for data component types
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Coined.MOD_ID); //the register for attachment types
    

    //======================================== Mod Data Types ========================================\\
    //==================== Data Component Types ====================\\
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Money>> MONEY = registerDataComponentType("money", builder -> builder.persistent(
        RecordCodecBuilder.create(instance -> instance.group( //the codec for the Money Class
            Codec.INT.fieldOf("Copper").forGetter(Money::getCopperAmount),
            Codec.INT.fieldOf("Silver").forGetter(Money::getSilverAmount),
            Codec.INT.fieldOf("Gold").forGetter(Money::getGoldAmount),
            Codec.INT.fieldOf("Platinum").forGetter(Money::getPlatinumAmount)
        ).apply(instance, Money::new))));

    //=================== Attachment Types ====================\\
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> PLAYER_BALANCE = registerAttachmentType("balance",
            () -> 0L, //default value
            b -> b.serialize(Codec.LONG)
            .copyOnDeath()
        );

    //======================================== Register Methods ========================================\\
    //==================== Data Component Types ====================\\
    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> registerDataComponentType(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES_REGISTER.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }
    
    //==================== Attachment Types ====================\\
    private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> registerAttachmentType(String name, Supplier<T> defaultSupplier, UnaryOperator<AttachmentType.Builder<T>> builderOp) {
        return ATTACHMENT_TYPES_REGISTER.register(name, () -> builderOp.apply(AttachmentType.builder(defaultSupplier)).build());
    }


    //--------------------------------------------------------------------------------\\
    /**
     * Register Data Component Types
     * @param modEventBus The mod event bus to register the data component types to
    */
    public static void register(IEventBus modEventBus) {
        Coined.LOGGER.info("Registering Data Stuff (Idk I'm scared I just want to save nbt data twt)...");
        DATA_COMPONENT_TYPES_REGISTER.register(modEventBus); //Register the data component types
        ATTACHMENT_TYPES_REGISTER.register(modEventBus); //Register the attachment types
    }
    
}
