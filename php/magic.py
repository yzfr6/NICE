import requests
import json

auth = { 'email': 'jared.leo@nice.org.uk', 'password': 'matnov-sonsuZ-8senje' }
url = 'https://api-test.magicapp.org/auth/login'

r = requests.post(url, data=auth)

print(r)

authToken = r.json()['token']
headers = { 'Authorization': f'Bearer {authToken}' }

r = requests.get('https://api.magicapp.org/api/v1/guidelines/2', headers=headers)
