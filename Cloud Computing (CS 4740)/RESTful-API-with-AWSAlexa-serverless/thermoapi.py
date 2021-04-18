from flask import Flask, json, request, make_response
import os
temperatures = [80,72]
api = Flask(__name__)

@api.route('/ThermsAreUs/api/v1.0/current-temp', methods=['GET'])
def get_currentTemp():
  return json.dumps(temperatures[0])

@api.route('/ThermsAreUs/api/v1.0/current-setpoint', methods=['GET'])
def get_desiredTemp():
    return json.dumps(temperatures[1])

@api.route('/ThermsAreUs/api/v1.0/current-setpoint', methods=['PUT'])
def set_desiredTemp():
    temperatures[1] = request.data
    return json.dumps(temperatures[1]),201


if __name__ == '__main__':
    api.run(debug = True,host="0.0.0.0", port=int(os.environ.get("PORT",5000)))
