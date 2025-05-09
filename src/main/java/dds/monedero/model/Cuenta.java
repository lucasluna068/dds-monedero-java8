package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private double limiteBase = 1000;
  private List<Movimiento> movimientos = new ArrayList<>();

  // CODESMELL. Se removieron los constructors de CUENTA
  // Al no tener el requerimiento funcional. Decidi hacer la clase mas simple y usar setSaldo para inicializar un saldo custom.


  public void poner(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (getMovimientos().stream()
        .filter(movimiento -> movimiento.fueDepositado(LocalDate.now()))
        .count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    new Movimiento(LocalDate.now(), cuanto, true).agregateA(this); //CODESMELL (Misplaced Method) MISMO CASO EN MOVIMIENTO
  }

  public void sacar(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }

    if (cuanto > limite()) {
      throw new MaximoExtraccionDiarioException(
          "No puede extraer mas de $ " + 1000 + " diarios, " + "límite: " + limite());
    }
    new Movimiento(LocalDate.now(), cuanto, false).agregateA(this); //CODESMELL (Misplaced Method) MISMO CASO EN MOVIMIENTO
  }

  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    var movimiento = new Movimiento(fecha, cuanto, esDeposito); //CODESMELL No hace falta agregar una variable.
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha)) //CODESMELL CODIGO REPETIDO. Cambiar por fueExtraido()
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

  //CODESMELL (Temporary Field). Se removieron las variables dentro de sacar() debido a que se creaban y utilizaban temporalmente ahi
  //Se creo un limiteBase seteado en el limite original. Pudiendo modificarse mediante un setter
  //Ahora limite es calculado mediante la diferencia entre el limite base y el monto extraido del dia.

  public void setLimiteBase(double limiteB) {
    this.limiteBase = limiteB;
  }

  public double limite() {
    return limiteBase - getMontoExtraidoA(LocalDate.now());
  }
}
