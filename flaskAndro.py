from flask import Flask, jsonify, request
from flask_cors import CORS
import mysql.connector

app = Flask(__name__)
CORS(app)

cnx = mysql.connector.connect(user='root', password='1234', host='127.0.0.1', database='IoTAndroid')

#main & 1st

@app.route('/lampMenuPOST', methods=['POST'])
def lampMenuPOST():
    data = request.get_json()
    
    r = data['R']
    g = data['G']
    b = data['B']
    intensity = data['Intensity']
    
    try:
        with cnx.cursor() as cursor:            
            sql = ("INSERT INTO lampMenu(r,g,b,intensity)"
                   "VALUES (%s, %s, %s,%s)")
            
            cursor.execute(sql,(r,g,b,intensity))
            
            cnx.commit()
            
            if cnx.is_connected():
                print("Connected to MySQL server")
            else:
                print("Connection failed")

            print(f"R : {r}, G : {g}, B : {b}, Intensity: {intensity}")

            return "Data received"
    except:
        return "Error"


@app.route('/lampMenuGET', methods=['GET'])
def lampMenuGET():
    try:
        with cnx.cursor() as cursor:
            sql = "SELECT * FROM lampMenu ORDER BY id DESC LIMIT 1"
            cursor.execute(sql)

            result = cursor.fetchall()
            data = {"id" : [], "r": [], "g": [], "b": [], "intensity": []}

            for row in result:
                data["id"].append(row[0])
                data["r"].append(row[1])
                data["g"].append(row[2])
                data["b"].append(row[3])
                data["intensity"].append(row[4])

            return jsonify(data)

    except Exception as e:
        print(e)
        return "Error"



#2nd

@app.route('/waterPOST', methods=['POST'])
def waterPOST():
    data = request.get_json()
    
    water = data['water_V']
   
    try:
        with cnx.cursor() as cursor:            
            sql = ("INSERT INTO water(water_Value)"
                   "VALUES (%s)")
            
            cursor.execute(sql,(water,))
            
            cnx.commit()
            
            if cnx.is_connected():
                print("Connected to MySQL server")
            else:
                print("Connection failed")

            print(f"temp C : {water}")

            return "Data received"
    except:
        return "Error"

@app.route('/waterGET', methods=['GET'])
def waterGET():
    try:
        with cnx.cursor() as cursor:
            sql = "SELECT * FROM Water ORDER BY id DESC LIMIT 1"
            cursor.execute(sql)

            result = cursor.fetchall()
            data = {"id" : [], "water_Value": []}

            for row in result:
                data["id"].append(row[0])
                data["water_Value"].append(row[1])

            return jsonify(data)

    except Exception as e:
        print(e)
        return "Error"


################################

@app.route('/tempPOST', methods=['POST'])
def tempPOST():
    data = request.get_json()
    
    temp_C = data['temp_C']
    
    try:
        with cnx.cursor() as cursor:            
            sql = ("INSERT INTO temp(temp_C)"
                   "VALUES (%s)")
            
            cursor.execute(sql,(temp_C,))
            
            cnx.commit()
            
            if cnx.is_connected():
                print("Connected to MySQL server")
            else:
                print("Connection failed")

            print(f"temp C : {temp_C}")

            return "Data received"
    except:
        return "Error"
    
@app.route('/tempGET', methods=['GET'])
def tempGET():
    try:
        with cnx.cursor() as cursor:
            sql = "SELECT * FROM temp ORDER BY id DESC LIMIT 1"
            cursor.execute(sql)

            result = cursor.fetchall()
            data = {"id" : [], "temp_C": []}

            for row in result:
                data["id"].append(row[0])
                data["temp_C"].append(row[1])

            return jsonify(data)

    except Exception as e:
        print(e)
        return "Error"
    
#3rd

@app.route('/doorPOST', methods=['POST'])
def doorPOST():

    data = request.get_json()
    
    door = data['Door']
    
    try:
        with cnx.cursor() as cursor:            
                sql = ("INSERT INTO door(door_val)"
                    "VALUES (%s)")
                
                cursor.execute(sql, (door,))
                
                cnx.commit()
                
                if cnx.is_connected():
                    print("Connected to MySQL server")
                else:
                    print("Connection failed")

                print(f"Door Value : {door}")

                return "Data received"
    except:
        return "Error"



@app.route('/doorGET', methods=['GET'])
def doorGET():
    try:
        with cnx.cursor() as cursor:
            sql = "SELECT * FROM door ORDER BY id DESC LIMIT 1"
            cursor.execute(sql)

            result = cursor.fetchall()
            data = {"id": [], "doorValue": []}

            for row in result:
                data["id"].append(row[0])
                data["doorValue"].append(row[1])
                
            return jsonify(data)

    except Exception as e:
        print(e)
        return "Error"


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')

#curl -X GET http://192.168.1.13:5000/data
#curl -X POST http://192.168.1.13:5000/switchPOST -H "Content-Type: application/json" --data "{\"light_status\":\"ON\",\"status_int\":\"1\"}"

#14:38:14.292 -> 1C D9 8D 64 

#14:38:17.757 -> B0 83 B8 21 
