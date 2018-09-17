package de.ecreators.ai.property.xor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.ecreators.ai.parent.network.NeuralNetworkMemory;

/**
 * @author Bjoern Frohberg
 */
public class XorAiPropertyTest {

  private final NeuralNetworkMemory xorMemory = null;

  @Test
  public void testXor_0_0__0() {
    final XorAiProperty property = new XorAiProperty(xorMemory);
    assertFalse(property.isXor(false, false));
  }

  @Test
  public void testXor_0_1__1() {
    final XorAiProperty property = new XorAiProperty(xorMemory);
    assertTrue(property.isXor(false, true));
  }

  @Test
  public void testXor_1_0__1() {
    final XorAiProperty property = new XorAiProperty(xorMemory);
    assertTrue(property.isXor(true, false));
  }

  @Test
  public void testXor_1_1__0() {
    final XorAiProperty property = new XorAiProperty(xorMemory);
    assertFalse(property.isXor(true, true));
  }
}
