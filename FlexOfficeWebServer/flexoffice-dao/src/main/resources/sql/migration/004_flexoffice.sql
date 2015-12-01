
ALTER TYPE gatewayStatus
	ADD VALUE 'ERROR_NO_USB_DEVICE'; -- appends to list
ALTER TYPE gatewayStatus
	ADD VALUE 'ERROR_FIFO_FILE'; -- appends to list

ALTER TYPE sensorStatus
	ADD VALUE 'UNSTABLE_VOLTAGE'; -- appends to list
ALTER TYPE sensorStatus
	ADD VALUE 'UNSTABLE_RSSI'; -- appends to list
