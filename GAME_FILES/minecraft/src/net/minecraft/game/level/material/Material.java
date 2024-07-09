package net.minecraft.game.level.material;

public class Material {
	public static final Material air = new MaterialTransparent();
	public static final Material ground = new Material();
	public static final Material wood = new Material();
	public static final Material rock = new Material();
	public static final Material iron = new Material();
	public static final Material water = new MaterialLiquid();
	public static final Material lava = new MaterialLiquid();
	public static final Material leaves = new Material();
	public static final Material plants = new MaterialLogic();
	public static final Material sponge = new Material();
	public static final Material cloth = new Material();
	public static final Material fire = new MaterialTransparent();
	public static final Material sand = new Material();
	public static final Material circuits = new MaterialLogic();
	public static final Material glass = new Material();
	public static final Material tnt = new Material();

	public boolean getIsLiquid() {
		return false;
	}

	public final boolean liquidSolidCheck() {
		return !this.getIsLiquid() && !this.isSolid();
	}

	public boolean isSolid() {
		return true;
	}

	public boolean getCanBlockGrass() {
		return true;
	}

	public boolean getIsSolid() {
		return true;
	}
}
