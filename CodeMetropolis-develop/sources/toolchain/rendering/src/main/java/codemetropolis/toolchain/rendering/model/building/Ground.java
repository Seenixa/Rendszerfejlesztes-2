package codemetropolis.toolchain.rendering.model.building;

import codemetropolis.toolchain.commons.cmxml.Buildable;
import codemetropolis.toolchain.commons.cmxml.Buildable.Type;
import codemetropolis.toolchain.commons.cmxml.Point;
import codemetropolis.toolchain.commons.model.BlockType;
import codemetropolis.toolchain.commons.model.property.Orientation8;
import codemetropolis.toolchain.rendering.exceptions.BuildingTypeMismatchException;
import codemetropolis.toolchain.rendering.model.pattern.RepeationPattern;
import codemetropolis.toolchain.rendering.model.primitive.Primitive;
import codemetropolis.toolchain.rendering.model.primitive.SignPost;
import codemetropolis.toolchain.rendering.model.primitive.SolidBox;
import codemetropolis.toolchain.rendering.util.Orientation;

import java.util.LinkedList;

import static codemetropolis.toolchain.commons.model.BlockType.STONE;
import static codemetropolis.toolchain.commons.model.BlockType.STONE_BRICKS;

public class Ground extends Building {

	public Ground(Buildable innerBuildable) throws BuildingTypeMismatchException {
		super(innerBuildable);

		if ( innerBuildable.getType() != Type.GROUND )
			throw new BuildingTypeMismatchException(innerBuildable.getType(), getClass());

		primitives.addAll(prepareBase());
		primitives.addAll(prepareSigns());
	}
	
	protected LinkedList<Primitive> prepareBase( ) {
		LinkedList<Primitive> base = new LinkedList<>();
		base.add(
			new SolidBox(
				position,
				new Point( size.getX(), 1, size.getZ() ),
				new RepeationPattern( new BlockType[][][]{ { { STONE } } } ),
				new RepeationPattern( new BlockType[][][] { { { STONE_BRICKS } } } ),
				Orientation.NearX ) );
		return base;
	}

	protected LinkedList<Primitive> prepareSigns( ) {
		LinkedList<Primitive> signs = new LinkedList<>();
		signs.add(new SignPost(position.getX(), position.getY() + 1, position.getZ(), Orientation8.NORTHWEST, innerBuildable.getName()));
		signs.add(new SignPost(position.getX() + size.getX() - 1, position.getY() + 1, position.getZ(), Orientation8.NORTHEAST, innerBuildable.getName()));
		signs.add(new SignPost(position.getX(), position.getY() + 1, position.getZ() + size.getZ() - 1, Orientation8.SOUTHWEST, innerBuildable.getName()));
		signs.add(new SignPost(position.getX() + size.getX() - 1, position.getY() + 1, position.getZ() + size.getZ() - 1, Orientation8.SOUTHEAST, innerBuildable.getName()));
		return signs;
	}

}
