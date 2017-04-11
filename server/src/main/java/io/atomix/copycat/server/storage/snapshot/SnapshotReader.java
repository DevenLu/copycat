/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package io.atomix.copycat.server.storage.snapshot;

import java.nio.charset.Charset;

import io.atomix.catalyst.buffer.Buffer;
import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.Bytes;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.util.Assert;

/**
 * Reads bytes from a state machine {@link Snapshot}.
 * <p>
 * This class provides the primary interface for reading snapshot buffers from disk or memory.
 * Snapshot bytes are read from an underlying {@link Buffer} which is backed by either memory
 * or disk based on the configured {@link io.atomix.copycat.server.storage.StorageLevel}.
 * <p>
 * In addition to standard {@link BufferInput} methods, snapshot readers support reading serializable objects
 * from the snapshot via the {@link #readObject()} method. Serializable types must be registered on the
 * {@link io.atomix.copycat.server.CopycatServer} serializer to be supported in snapshots.
 *
 * @author <a href="http://github.com/kuujo>Jordan Halterman</a>
 */
public class SnapshotReader implements BufferInput<SnapshotReader> {
  private final Buffer buffer;
  private final Snapshot snapshot;
  private final Serializer serializer;

  SnapshotReader(Buffer buffer, Snapshot snapshot, Serializer serializer) {
    this.buffer = Assert.notNull(buffer, "buffer");
    this.snapshot = Assert.notNull(snapshot, "snapshot");
    this.serializer = Assert.notNull(serializer, "serializer");
  }

  @Override
  public long remaining() {
    return buffer.remaining();
  }

  @Override
  public boolean hasRemaining() {
    return buffer.hasRemaining();
  }

  @Override
  public SnapshotReader skip(long bytes) {
    buffer.skip(bytes);
    return this;
  }

  /**
   * Reads an object from the buffer.
   *
   * @param <T> The type of the object to read.
   * @return The read object.
   */
  public <T> T readObject() {
    return serializer.readObject(buffer);
  }

  @Override
  public SnapshotReader read(Bytes bytes) {
    buffer.read(bytes);
    return this;
  }

  @Override
  public SnapshotReader read(byte[] bytes) {
    buffer.read(bytes);
    return this;
  }

  @Override
  public SnapshotReader read(Bytes bytes, long offset, long length) {
    buffer.read(bytes, offset, length);
    return this;
  }

  @Override
  public SnapshotReader read(byte[] bytes, long offset, long length) {
    buffer.read(bytes, offset, length);
    return this;
  }

  @Override
  public SnapshotReader read(Buffer buffer) {
    this.buffer.read(buffer);
    return this;
  }

  @Override
  public int readByte() {
    return buffer.readByte();
  }

  @Override
  public int readUnsignedByte() {
    return buffer.readUnsignedByte();
  }

  @Override
  public char readChar() {
    return buffer.readChar();
  }

  @Override
  public short readShort() {
    return buffer.readShort();
  }

  @Override
  public int readUnsignedShort() {
    return buffer.readUnsignedShort();
  }

  @Override
  public int readMedium() {
    return buffer.readMedium();
  }

  @Override
  public int readUnsignedMedium() {
    return buffer.readUnsignedMedium();
  }

  @Override
  public int readInt() {
    return buffer.readInt();
  }

  @Override
  public long readUnsignedInt() {
    return buffer.readUnsignedInt();
  }

  @Override
  public long readLong() {
    return buffer.readLong();
  }

  @Override
  public float readFloat() {
    return buffer.readFloat();
  }

  @Override
  public double readDouble() {
    return buffer.readDouble();
  }

  @Override
  public boolean readBoolean() {
    return buffer.readBoolean();
  }

  @Override
  public String readString() {
    return buffer.readString();
  }

  @Override
  public String readString(Charset charset) {
    return buffer.readString(charset);
  }

  @Override
  public String readUTF8() {
    return buffer.readUTF8();
  }

  @Override
  public void close() {
    buffer.close();
    snapshot.closeReader(this);
  }

}
