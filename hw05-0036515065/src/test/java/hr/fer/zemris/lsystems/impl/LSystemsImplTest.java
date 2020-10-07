package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import org.junit.jupiter.api.Test;
import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.lsystems.impl.commands.*;
import hr.fer.zemris.math.Vector2D;

class LSystemsImplTest {

	@Test
	public void generateTest() {

		LSystemBuilderImpl test = new LSystemBuilderImpl();
		//test.setAxiom("F").registerProduction('F', "F+F--F+F");
		test.registerProduction('F', "F+F--F+F").setAxiom("F");

		assertEquals("F", test.build().generate(0));
		assertEquals("F+F--F+F", test.build().generate(1));
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", test.build().generate(2));
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F+F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F+F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", test.build().generate(3));
	}

	@Test
	public void turtleCopyTest() {
		TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
		TurtleState state2 = state.copy();

		assertEquals(state.getPosition().getX(), state2.getPosition().getX());
		assertEquals(state.getPosition().getY(), state2.getPosition().getY());
		assertEquals(state.getDirection().getX(), state2.getDirection().getX());
		assertEquals(state.getDirection().getY(), state2.getDirection().getY());
		assertEquals(state.getColor(), state2.getColor());
		assertEquals(state.getShift(), state2.getShift());

		assertEquals(20, state.getPosition().getX());
		assertEquals(15, state.getPosition().getY());
		assertEquals(1, state.getDirection().getX());
		assertEquals(0, state.getDirection().getY());
		assertEquals(Color.BLACK, state.getColor());
		assertEquals(20, state.getShift());

		state2.getPosition().translate(state2.getPosition());
		state2.getDirection().translate(state2.getDirection());
		state2.setColor(Color.GREEN);
		state2.setShift(10);

		assertEquals(40, state2.getPosition().getX());
		assertEquals(30, state2.getPosition().getY());
		assertEquals(2, state2.getDirection().getX());
		assertEquals(0, state2.getDirection().getY());
		assertEquals(Color.GREEN, state2.getColor());
		assertEquals(10, state2.getShift());

		assertEquals(20, state.getPosition().getX());
		assertEquals(15, state.getPosition().getY());
		assertEquals(1, state.getDirection().getX());
		assertEquals(0, state.getDirection().getY());
		assertEquals(Color.BLACK, state.getColor());
		assertEquals(20, state.getShift());
	}

	@Test
	public void contextGetCurrentStateTest() {
		Context ctx = new Context();
		TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
		ctx.pushState(state);
		TurtleState state2 = ctx.getCurrentState();
		TurtleState state3 = ctx.getCurrentState();

		assertEquals(state, state2);
		assertEquals(state, state3);
	}

	@Test
	public void popCommandTest() {
		Context ctx = new Context();
		TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
		ctx.pushState(state);
		PopCommand comm = new PopCommand();
		comm.execute(ctx, null);
		try {
			ctx.getCurrentState();
			fail("Should fail");
		} catch (EmptyStackException ex) {

		}

		TurtleState state2 = state.copy();
		ctx.pushState(state);
		ctx.pushState(state2);
		comm.execute(ctx, null);
		assertEquals(state, ctx.getCurrentState());
	}

	@Test
	public void pushCommandTest() {
		Context ctx = new Context();
		TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
		ctx.pushState(state);
		PushCommand comm = new PushCommand();
		comm.execute(ctx, null);

		assertNotEquals(state, ctx.getCurrentState());
		assertEquals(state.getColor(), ctx.getCurrentState().getColor());
		assertEquals(state.getShift(), ctx.getCurrentState().getShift());
		assertEquals(state.getPosition().getX(), ctx.getCurrentState().getPosition().getX());
		assertEquals(state.getPosition().getY(), ctx.getCurrentState().getPosition().getY());
		assertEquals(state.getDirection().getX(), ctx.getCurrentState().getDirection().getX());
		assertEquals(state.getDirection().getY(), ctx.getCurrentState().getDirection().getY());
	}

	@Test
	public void rotateCommandTest() {
		Context ctx = new Context();
		TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
		ctx.pushState(state);
		RotateCommand comm = new RotateCommand(90);
		comm.execute(ctx, null);

		assertEquals(0, state.getDirection().getX(), 1E-3);
		assertEquals(1, state.getDirection().getY(), 1E-3);
	}

	@Test
	public void skipCommandTest() {
		Context ctx = new Context();
		TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
		ctx.pushState(state);
		SkipCommand comm = new SkipCommand(2);
		comm.execute(ctx, null);

		assertEquals(60, state.getPosition().getX());
		assertEquals(15, state.getPosition().getY());
	}

	@Test
	public void scaleCommandTest() {
		Context ctx = new Context();
		TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
		ctx.pushState(state);
		ScaleCommand comm = new ScaleCommand(2);
		comm.execute(ctx, null);

		assertEquals(40, state.getShift());
	}

	@Test
	public void colorCommandTest() {
		Context ctx = new Context();
		TurtleState state = new TurtleState(new Vector2D(20, 15), new Vector2D(1, 0), Color.BLACK, 20);
		ctx.pushState(state);
		ColorCommand comm = new ColorCommand(Color.CYAN);
		comm.execute(ctx, null);

		assertEquals(Color.CYAN, state.getColor());
	}

}
