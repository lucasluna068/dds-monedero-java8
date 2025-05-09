package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MovimientoTest {

  private Movimiento movimientoD;
  private Movimiento movimientoE;

  @BeforeEach
  void init() {

    movimientoD = new Movimiento(LocalDate.now(),1000,true);
    movimientoE = new Movimiento(LocalDate.of(2025, 5, 9),7000,false);
  }

  @Test
  @DisplayName("El valor del Deposito es 1000$")
  void ElValorDelDeposito() {
    assertEquals(1000,movimientoD.getMonto());
  }


  @Test
  @DisplayName("Un Deposito es un Deposito")
  void ElDepositoEsDeposito() {
    assertTrue(movimientoD.isDeposito());
  }

  @Test
  @DisplayName("Un Deposito no es Extraccion")
  void ElDepositoNoEsExtraccion() {
    assertFalse(movimientoD.isExtraccion());
  }

  @Test
  @DisplayName("Un Deposito fecha correcta")
  void ElDepositoFechaCorrecta() {
    assertTrue(movimientoD.fueDepositado(LocalDate.now()));
  }

  @Test
  @DisplayName("Un Deposito fecha incorrecta")
  void ElDepositoFechaIncorrecta() {
    assertFalse(movimientoD.fueDepositado(LocalDate.of(2025, 5, 9)));
  }

  @Test
  @DisplayName("Un Extraccion fue depositada con fecha Correcta")
  void LaExtraccionDepositadoFechaCorrecta() {
    assertFalse(movimientoE.fueDepositado(LocalDate.of(2025, 5, 9)));
  }

  @Test
  @DisplayName("Una Extraccion no es un Deposito")
  void LaExtraccionNoEsDeposito() {
    assertEquals(false,movimientoE.isDeposito());
  }

  @Test
  @DisplayName("Una Extraccion es Extraccion")
  void LaExtraccionEsExtraccion() {
    assertEquals(true,movimientoE.isExtraccion());
  }

  @Test
  @DisplayName("Una Extraccion fecha correcta")
  void LaExtraccionFechaCorrecta() {
    assertTrue(movimientoE.fueExtraido(LocalDate.of(2025, 5, 9)));
  }

  @Test
  @DisplayName("Una Extraccion fecha incorrecta")
  void LaExtraccionFechaIncorrecta() {
    assertFalse(movimientoE.fueExtraido(LocalDate.now()));
  }

  @Test
  @DisplayName("Un Deposito fue extraido fecha correcta")
  void ElDepositoExtraccionFechaCorrecta() {
    assertFalse(movimientoD.fueExtraido(LocalDate.now()));
  }

}
