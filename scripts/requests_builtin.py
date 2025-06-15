import json
from urllib import request, error

APPLICATION_JSON = 'application/json'


def get(url, params=None, headers=None):
    result_url = url
    if params:
        query_string = '&'.join(f"{key}={value}" for key, value in params.items())
        result_url += '?' + query_string
    default_headers = {'Accept': APPLICATION_JSON}
    result_headers = {**default_headers, **(headers or {})}
    req = request.Request(result_url, headers=result_headers)
    try:
        with request.urlopen(req) as response:
            resp_str = response.read().decode().strip()
            return json.loads(resp_str) if resp_str else None
    except error.HTTPError as e:
        print(f"HTTP Error: {e.code} - {e.reason}")
        raise e
    except error.URLError as e:
        print(f"URL Error: {e.reason}")
        raise e


def post(url, data, params=None, headers=None):
    result_url = url
    if params:
        query_string = '&'.join(f"{key}={value}" for key, value in params.items())
        result_url += '?' + query_string
    default_headers = {'Accept': APPLICATION_JSON, 'Content-Type': APPLICATION_JSON}
    result_headers = {**default_headers, **(headers or {})}
    req = request.Request(result_url, data=json.dumps(data).encode(), headers=result_headers, method='POST')
    try:
        with request.urlopen(req) as response:
            resp_str = response.read().decode().strip()
            return json.loads(resp_str) if resp_str else None
    except error.HTTPError as e:
        print(f"HTTP Error: {e.code} - {e.reason}")
        raise e
    except error.URLError as e:
        print(f"URL Error: {e.reason}")
        raise e

def put(url, data, params=None, headers=None):
    result_url = url
    if params:
        query_string = '&'.join(f"{key}={value}" for key, value in params.items())
        result_url += '?' + query_string
    default_headers = {'Accept': APPLICATION_JSON, 'Content-Type': APPLICATION_JSON}
    result_headers = {**default_headers, **(headers or {})}
    req = request.Request(result_url, data=json.dumps(data).encode(), headers=result_headers, method='PUT')
    try:
        with request.urlopen(req) as response:
            resp_str = response.read().decode().strip()
            return json.loads(resp_str) if resp_str else None
    except error.HTTPError as e:
        print(f"HTTP Error: {e.code} - {e.reason}")
        raise e
    except error.URLError as e:
        print(f"URL Error: {e.reason}")
        raise e
