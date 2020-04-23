/*
 * Copyright © 2020 NHSX. All rights reserved.
 */

package uk.nhs.nhsx.sonar.android.app.ble

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import java.util.UUID

val UPDATE_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
val COLOCATE_SERVICE_UUID: UUID = UUID.fromString("c1f5983c-fa94-4ac8-8e2e-bb86d6de9b21")
val COLOCATE_KEEPALIVE_CHARACTERISTIC_UUID: UUID = UUID.fromString("D802C645-5C7B-40DD-985A-9FBEE05FE85C")
val DEVICE_CHARACTERISTIC_UUID: UUID = UUID.fromString("85BF337C-5B64-48EB-A5F7-A9FED135C972")

fun BluetoothGattCharacteristic.isDeviceIdentifier(): Boolean =
    this.uuid == DEVICE_CHARACTERISTIC_UUID

fun BluetoothGattCharacteristic.isKeepAlive(): Boolean =
    this.uuid == COLOCATE_KEEPALIVE_CHARACTERISTIC_UUID

fun BluetoothGattDescriptor.isNotifyDescriptor(): Boolean =
    this.uuid == UPDATE_DESCRIPTOR_UUID
