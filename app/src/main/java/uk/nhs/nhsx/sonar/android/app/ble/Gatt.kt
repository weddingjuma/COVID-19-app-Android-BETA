/*
 * Copyright © 2020 NHSX. All rights reserved.
 */

package uk.nhs.nhsx.sonar.android.app.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt.GATT_FAILURE
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattCharacteristic.PERMISSION_READ
import android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothGattService.SERVICE_TYPE_PRIMARY
import android.bluetooth.BluetoothManager
import android.content.Context
import timber.log.Timber
import uk.nhs.nhsx.sonar.android.app.crypto.BluetoothCryptogramProvider
import uk.nhs.nhsx.sonar.android.app.di.module.BluetoothModule.Companion.ENCRYPT_SONAR_ID
import uk.nhs.nhsx.sonar.android.app.registration.SonarIdProvider
import javax.inject.Inject
import javax.inject.Named

class Gatt @Inject constructor(
    private val context: Context,
    private val bluetoothManager: BluetoothManager,
    private val sonarIdProvider: SonarIdProvider,
    private val bluetoothCryptogramProvider: BluetoothCryptogramProvider,
    @Named(ENCRYPT_SONAR_ID)
    private val encryptSonarId: Boolean
) {
    private val identifier: Identifier
        get() = if (encryptSonarId) {
            Identifier.fromBytes(bluetoothCryptogramProvider.provideBluetoothCryptogram().asBytes())
        } else {
            Identifier.fromString(sonarIdProvider.getSonarId())
        }

    private val service: BluetoothGattService = BluetoothGattService(
        COLOCATE_SERVICE_UUID,
        SERVICE_TYPE_PRIMARY
    )
        .also {
            it.addCharacteristic(
                BluetoothGattCharacteristic(
                    DEVICE_CHARACTERISTIC_UUID,
                    PROPERTY_READ,
                    PERMISSION_READ
                )
            )
        }

    private var server: BluetoothGattServer? = null

    fun start() {
        Timber.d("Bluetooth Gatt start")
        val callback = object : BluetoothGattServerCallback() {
            override fun onCharacteristicReadRequest(
                device: BluetoothDevice,
                requestId: Int,
                offset: Int,
                characteristic: BluetoothGattCharacteristic
            ) {
                Timber.d("Bluetooth onCharacteristicReadRequest")
                if (characteristic.isDeviceIdentifier()) {
                    server?.sendResponse(
                        device,
                        requestId,
                        GATT_SUCCESS,
                        0,
                        identifier.asBytes
                    )
                } else {
                    server?.sendResponse(
                        device,
                        requestId,
                        GATT_FAILURE,
                        0,
                        byteArrayOf()
                    )
                }
            }
        }

        server = bluetoothManager.openGattServer(context, callback).also {
            it.addService(service)
        }
    }

    fun stop() {
        Timber.d("Bluetooth Gatt stop")
        server?.close()
    }
}
