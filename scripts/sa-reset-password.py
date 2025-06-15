#!/usr/bin/env python3

import argparse
import getpass
import time

from requests_builtin import post, put

argparser = argparse.ArgumentParser(description='Reset Super Admin password')
argparser.add_argument(
    '-a', '--auth-service-url',
    default='http://localhost:8080',
    type=str,
    required=False,
    help='URL of the auth-service',
)
argparser.add_argument(
    '-o', '--otp-service-url',
    default='http://localhost:8084',
    type=str,
    required=False,
    help='URL of the otp-service',
)
argparser.add_argument(
    '-e', '--email',
    default='super.admin@jwt.example.dburyak.com',
    type=str,
    required=False,
    help='Email of the super admin',
)
argparser.add_argument(
    '-d', '--device-id',
    default='sa-reset-password-script',
    type=str,
    required=False,
    help='Device ID',
)
args = argparser.parse_args()

header_tenant_uuid = 'x-tenant-uuid'
sa_tenant_uuid = '00000000-0000-0000-0000-000000000000'
param_device_id = 'deviceId'

print('creating password-reset OTP')
post(args.auth_service_url + '/users/anonymous/password/reset/otp',
    headers={header_tenant_uuid: sa_tenant_uuid},
    data={
        'locale': 'en',
        'deviceId': args.device_id,
        'externalId': {
            'email': args.email,
        },
    })
time.sleep(2)  # wait for a couple of seconds for OTP to be sent

print('getting OTP code')
otp_resp = post(args.otp_service_url + '/otps/anonymous/password-reset/get',
    params={param_device_id: args.device_id},
    headers={header_tenant_uuid: sa_tenant_uuid},
    data={
        'email': args.email,
    })
otp_code = otp_resp['code']

# Ask for the new password in secure way
new_password = getpass.getpass('Enter new SA password: ').strip()
new_password_confirm = getpass.getpass('Confirm new SA password: ').strip()
if new_password != new_password_confirm:
    print('Passwords do not match. Exiting.')
    exit(1)
print('resetting password')
put(args.auth_service_url + '/users/anonymous/password/reset',
    headers={header_tenant_uuid: sa_tenant_uuid},
    data={
        'deviceId': args.device_id,
        'externalId': {
            'email': args.email,
        },
        'otp': otp_code,
        'newPassword': new_password,
    })

print('done')
