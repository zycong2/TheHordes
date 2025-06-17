package org.zycong.theHordes.helpers.Traders;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

import static org.zycong.theHordes.TheHordes.Colorize;

public class MobNPC{

    private EntityType type;
    private String displayName = "";
    private boolean noAI = true;
    private boolean alwaysShowName = true;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack mainHand;
    private ItemStack offHand;

    private Location lastSpawnedLocation;

    private Consumer<RightClickContext> RightClickContext;
    private Consumer<LeftClickContext> LeftClickContext;
    private Consumer<DeathContext> DeathContext;
    private Consumer<DamageContext> DamageContext;

    private ItemStack Nothing = new ItemStack(Material.AIR);

    public MobNPC(EntityType et){
        this.type = et;
    }

    void respawn(){
        respawn(lastSpawnedLocation);
    }

    void respawn(Location loc){
        lastSpawnedLocation = loc;
        createEntity(type, loc);
    }

    void spawn(Player player){
        Location loc = player.getLocation();
        lastSpawnedLocation = loc;
        createEntity(type, loc);
    }

    void spawn(Location loc){
        createEntity(type, loc);
        lastSpawnedLocation = loc;
    }

    LivingEntity createEntity(EntityType type, Location loc){

        LivingEntity output = (LivingEntity) loc.getWorld().spawn(loc, type.getEntityClass());

        EntityEquipment equip = output.getEquipment();

        output.customName(Colorize(this.displayName));
        output.setAI(!this.noAI);
        output.setCustomNameVisible(this.alwaysShowName);

        equip.setHelmet(IsItemStackNULL(this.helmet) ? Nothing : this.helmet);equip.setHelmetDropChance(0f);
        equip.setChestplate(IsItemStackNULL(this.chestplate) ? Nothing : this.chestplate);equip.setChestplateDropChance(0f);
        equip.setLeggings(IsItemStackNULL(this.leggings) ? Nothing : this.leggings);equip.setLeggingsDropChance(0f);
        equip.setBoots(IsItemStackNULL(this.boots) ? Nothing : this.boots);equip.setBootsDropChance(0f);

        return output;
    }

    private boolean IsItemStackNULL(ItemStack itemStack){
        return itemStack == null;
    }

    public record RightClickContext(Player player, Action action) {}
    public record LeftClickContext(Player player, Action action) {}
    public record DeathContext(LivingEntity attacker) {}
    public record DamageContext(LivingEntity attacker, Double damage) {}

    // Getter setter code down there don't look!



























































































































































































































    // Come on was it really that hard to follow my comment?

    private String getDisplayName() {
        return displayName;
    }

    private MobNPC displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    private boolean isNoAI() {
        return noAI;
    }

    private MobNPC noAI(boolean noAI) {
        this.noAI = noAI;
        return this;
    }

    private boolean isAlwaysShowName() {
        return alwaysShowName;
    }

    private MobNPC alwaysShowName(boolean alwaysShowName) {
        this.alwaysShowName = alwaysShowName;
        return this;
    }

    private ItemStack getHelmet() {
        return helmet;
    }

    private MobNPC helmet(ItemStack helmet) {
        this.helmet = helmet;
        return this;
    }

    private ItemStack getChestplate() {
        return chestplate;
    }

    private MobNPC chestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
        return this;
    }

    private ItemStack getLeggings() {
        return leggings;
    }

    private MobNPC leggings(ItemStack leggings) {
        this.leggings = leggings;
        return this;
    }

    private ItemStack getBoots() {
        return boots;
    }

    private MobNPC boots(ItemStack boots) {
        this.boots = boots;
        return this;
    }

    private ItemStack getMainHand() {
        return mainHand;
    }

    private MobNPC mainHand(ItemStack mainHand) {
        this.mainHand = mainHand;
        return this;
    }

    private ItemStack getOffHand() {
        return offHand;
    }

    private MobNPC offHand(ItemStack offHand) {
        this.offHand = offHand;
        return this;
    }

    public Consumer<RightClickContext> getRightClickContext() {
        return RightClickContext;
    }

    public MobNPC rightClickContext(Consumer<RightClickContext> rightClickContext) {
        RightClickContext = rightClickContext;
        return this;
    }

    public Consumer<LeftClickContext> getLeftClickContext() {
        return LeftClickContext;
    }

    public MobNPC leftClickContext(Consumer<LeftClickContext> leftClickContext) {
        LeftClickContext = leftClickContext;
        return this;
    }

    public Consumer<DeathContext> getDeathContext() {
        return DeathContext;
    }

    public MobNPC deathContext(Consumer<DeathContext> deathContext) {
        DeathContext = deathContext;
        return this;
    }

    public Consumer<DamageContext> getDamageContext() {
        return DamageContext;
    }

    public MobNPC damageContext(Consumer<DamageContext> damageContext) {
        DamageContext = damageContext;
        return this;
    }

    // you doing this intentionally right? fine.

}
