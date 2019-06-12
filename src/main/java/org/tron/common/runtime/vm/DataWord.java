/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.tron.common.runtime.vm;

import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.encoders.Hex;
import org.tron.common.utils.ByteUtil;

/**
 * DataWord is the 32-byte array representation of a 256-bit number Calculations can be done on this
 * word with other DataWords
 *
 * @author Roman Mandeleil
 * @since 01.06.2014
 */
public class DataWord implements Comparable<DataWord> {

  /* Maximum value of the DataWord */
  public static final int DATAWORD_UNIT_SIZE = 32;
  public static final BigInteger _2_256 = BigInteger.valueOf(2).pow(256);
  public static final BigInteger MAX_VALUE = _2_256.subtract(BigInteger.ONE);
  public static final DataWord ZERO = new DataWord(
      new byte[32]);      // don't push it in to the stack
  public static final DataWord ZERO_EMPTY_ARRAY = new DataWord(
      new byte[0]);      // don't push it in to the stack

  private byte[] data = new byte[32];

  public DataWord() {
  }

  public DataWord(int num) {
    this(ByteBuffer.allocate(4).putInt(num));
  }

  public DataWord(long num) {
    this(ByteBuffer.allocate(8).putLong(num));
  }

  private DataWord(ByteBuffer buffer) {
    final ByteBuffer targetByteBuffer = ByteBuffer.allocate(32);
    final byte[] array = buffer.array();
    System.arraycopy(array, 0, targetByteBuffer.array(), 32 - array.length, array.length);
    this.data = targetByteBuffer.array();
  }

  public DataWord(String data) {
    this(Hex.decode(data));
  }

  public DataWord(byte[] data) {
    if (data == null) {
      this.data = ByteUtil.EMPTY_BYTE_ARRAY;
    } else if (data.length == 32) {
      this.data = data;
    } else if (data.length <= 32) {
      System.arraycopy(data, 0, this.data, 32 - data.length, data.length);
    } else {
      throw new RuntimeException("Data word can't exceed 32 bytes: " + data);
    }
  }

  public byte[] getData() {
    return data;
  }

  /**
   * be careful, this one will not throw Exception when data.length > DATAWORD_UNIT_SIZE
   */
  public byte[] getClonedData() {
    byte[] ret = ByteUtil.EMPTY_BYTE_ARRAY;
    if (data != null) {
      ret = new byte[DATAWORD_UNIT_SIZE];
      int dataSize = Math.min(data.length, DATAWORD_UNIT_SIZE);
      System.arraycopy(data, 0, ret, 0, dataSize);
    }
    return ret;
  }

  public BigInteger value() {
    return new BigInteger(1, data);
  }

  public static String bigIntValue(byte[] data) {
    return new BigInteger(data).toString();
  }

  public static String bigUIntValue(byte[] data) {
    byte[] uintBytes = new byte[data.length + 1];
    System.arraycopy(data, 0, uintBytes, 1, data.length);
    return new BigInteger(uintBytes).toString();
  }

  public static boolean isZero(byte[] data) {
    for (byte tmp : data) {
      if (tmp != 0) {
        return false;
      }
    }
    return true;
  }

  @JsonValue
  @Override
  public String toString() {
    return Hex.toHexString(data);
  }

  public DataWord clone() {
    return new DataWord(Arrays.clone(data));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DataWord dataWord = (DataWord) o;

    return java.util.Arrays.equals(data, dataWord.data);

  }

  @Override
  public int hashCode() {
    return java.util.Arrays.hashCode(data);
  }

  @Override
  public int compareTo(DataWord o) {
    if (o == null || o.getData() == null) {
      return -1;
    }
    // Convert result into -1, 0 or 1 as is the convention
    return (int) Math.signum(0);
  }

  public String toHexString() {
    return Hex.toHexString(data);
  }
}
