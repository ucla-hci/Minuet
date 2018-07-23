//	........................................................................................................
//
// 3d visualization for project minuet, v1.0
//
// by xiangchen@acm.org, 07/2018
// by runchank@andrew.cmu.edu  07/2018
//

// 🚨 🚨 🚨 🚨 🚨 🚨 🚨  NOTE! 🚨 🚨 🚨 🚨 🚨 🚨 🚨
// - in three.js, y is vertical, z is horizontal, x remains the same
//
// TO IMPROVE:
// - interpolation to make the animation smoother
//	........................................................................................................
// July2018 - Richard - added the MQTT section for incoming data stream


var XAC = XAC || {};
var MINUET = MINUET || {};
var host = "192.168.1.8";
var port = 9001;


//
//  ready function to initialize the vis
//
$(document).ready(function() {
  // load configuration and data
  YAML.load("config.yml", function(config) {


    Object.assign(MINUET, config);

    MINUET.setupScene(MINUET.room.lx, MINUET.room.ly, MINUET.room.lz);
    XAC.createUI();
    var mqtt;

    MINUET.dataLogs;

    MINUET.updateCurPoint("0 0 0 0 0 0");

          // visualize the user and anchors
    MINUET.user = addABall(MINUET.dataLogs.position, 0xff0000, 150);
    for (anchor of MINUET.anchors) {
      var posAnchor = new THREE.Vector3(anchor.x, anchor.z, anchor.y);
      addABall(posAnchor, 0x0000ff, 150);
    }
          



          function onFailure(message) {
            console.log("Connection Attempt to Host "+host+"Failed");
            setTimeout(MQTTconnect, 2000);
          }


          function onMessageArrived(msg){
            out_msg="Message received "+msg.payloadString;

            console.log(out_msg);
            MINUET.updateCurPoint(msg.payloadString);
            MINUET.animate();

          }

          function onConnect() {
            console.log("Connected ");
            mqtt.subscribe("locData");
            message = new Paho.MQTT.Message("mqtt Start");
            message.destinationName = "jsStatus";
            mqtt.send(message);
          }


          function MQTTconnect() {
            console.log("connecting to "+ host +" "+ port);
            mqtt = new Paho.MQTT.Client("192.168.1.8",9001,"clientjs");
            var options = {
              timeout: 3,
              onSuccess: onConnect,
              onFailure: onFailure,
              userName:"admin",
              password:"19930903",
            };
            mqtt.onMessageArrived = onMessageArrived

            mqtt.connect(options); 
          }

          MQTTconnect();
    });
});





//
// update the current data point when a new sample of data coming from mqtt
//

MINUET.updateCurPoint = function(rawData){
  console.log("got: "+ rawData);
  var singleData = rawData.split(" ");
  console.log("singleData: "+ singleData);
  var yaw = Number(singleData[3]-30);

  if(yaw <0 ){
    yaw = 360+yaw;
  }
  var pitch = Number(singleData[4]);
  var roll = Number(singleData[5]);
  var x = Number(singleData[0])+MINUET.armLength*Math.cos((pitch * Math.PI) / 180)*Math.sin((yaw * Math.PI) / 180);
  var y = Number(singleData[1])+MINUET.armLength*Math.cos((pitch * Math.PI) / 180)*Math.cos((yaw * Math.PI) / 180);
  var proxyZ = Number(singleData[2])-Math.sin((pitch * Math.PI) / 180)*MINUET.armLength;
  var z = 0;
  if(Math.abs(proxyZ-MINUET.hardcodedZ)>=Math.abs(proxyZ-MINUET.hardcodedZ2)){
    z = MINUET.hardcodedZ2;
  }
  else{
    z = MINUET.hardcodedZ;
  }
  console.log("x: "+ x);
  console.log("y: "+ y);
  console.log("z: "+ z);
  console.log("pitch: "+ pitch);
  console.log("yaw: "+ yaw);


  MINUET.dataLogs = {
          position: new THREE.Vector3(x, z, y),
          orientation: new THREE.Vector3(
            -Math.sin((yaw * Math.PI) / 180) *
              Math.cos((pitch * Math.PI) / 180),
            Math.sin((pitch * Math.PI) / 180),
            -Math.cos(((yaw) * Math.PI) / 180) *
              Math.cos((pitch * Math.PI) / 180)
          )
        }
        console.log(MINUET.dataLogs.position);
        console.log(MINUET.dataLogs.orientation);
}

//
//  set up the scene for visualization
//
MINUET.setupScene = function(lx, ly, lz) {
  // scene and view point
  XAC.scene = new THREE.Scene();
  XAC.objects = new Array();

  //  draw ground
  var lineMaterial = new THREE.LineBasicMaterial({
    color: MINUET.gridColor
  });
  var lineGeometry = new THREE.Geometry();
  var floor = 0;
  var step = 250;
  var nxSteps = (lx / step) | 0;
  for (var i = 0; i <= nxSteps; i++) {
    lineGeometry.vertices.push(new THREE.Vector3(i * step, floor, 0));
    lineGeometry.vertices.push(new THREE.Vector3(i * step, floor, ly));
  }
  var nzSteps = (ly / step) | 0;
  for (var i = 0; i <= nzSteps; i++) {
    lineGeometry.vertices.push(new THREE.Vector3(0, floor, i * step));
    lineGeometry.vertices.push(new THREE.Vector3(lx, floor, i * step));
  }

  lineGeometry.vertices.push(new THREE.Vector3(0, 0, 0));
  lineGeometry.vertices.push(new THREE.Vector3(0, lz, 0));
  lineGeometry.vertices.push(new THREE.Vector3(lx, 0, 0));
  lineGeometry.vertices.push(new THREE.Vector3(lx, lz, 0));
  lineGeometry.vertices.push(new THREE.Vector3(lx, 0, ly));
  lineGeometry.vertices.push(new THREE.Vector3(lx, lz, ly));
  lineGeometry.vertices.push(new THREE.Vector3(0, 0, ly));
  lineGeometry.vertices.push(new THREE.Vector3(0, lz, ly));

  lineGeometry.vertices.push(new THREE.Vector3(0, lz, 0));
  lineGeometry.vertices.push(new THREE.Vector3(lx, lz, 0));
  lineGeometry.vertices.push(new THREE.Vector3(lx, lz, 0));
  lineGeometry.vertices.push(new THREE.Vector3(lx, lz, ly));
  lineGeometry.vertices.push(new THREE.Vector3(lx, lz, ly));
  lineGeometry.vertices.push(new THREE.Vector3(0, lz, ly));
  lineGeometry.vertices.push(new THREE.Vector3(0, lz, ly));
  lineGeometry.vertices.push(new THREE.Vector3(0, lz, 0));

  var grid = new THREE.Line(lineGeometry, lineMaterial, THREE.LinePieces);
  XAC.scene.add(grid);

  // set up camera
  XAC.camera = new THREE.PerspectiveCamera(
    90,
    window.innerWidth / window.innerHeight,
    1,
    20000
    );

  var ratioCamPos = new THREE.Vector3(0.25, 2.5, 1.5).multiplyScalar(0.8);
  XAC.posCam = new THREE.Vector3(
    lx * ratioCamPos.x,
    lz * ratioCamPos.y,
    ly * ratioCamPos.z
    );
  XAC.camera.position.copy(XAC.posCam);

  XAC.lookAt = new THREE.Vector3(lx / 2, lz / 2, ly / 2);

  XAC.mouseCtrls = new THREE.TrackballControls(
    XAC.camera,
    undefined,
    XAC.lookAt
    );

  // set up mouse
  XAC.mouseCtrls.rotateSpeed = 5.0;
  XAC.mouseCtrls.zoomSpeed = 0.5;
  XAC.mouseCtrls.panSpeed = 2;

  XAC.mouseCtrls.noZoom = false;

  XAC.mouseCtrls.staticMoving = true;
  XAC.mouseCtrls.dynamicDampingFactor = 0.3;

  XAC.wheelDisabled = false;

  // add lights
  XAC.lights = [];
  XAC.lights[0] = new THREE.PointLight(0xffffff, 1, 0);
  XAC.lights[0].position.set(0, 10000, -10000);
  XAC.lights[0].castShadow = true;
  XAC.scene.add(XAC.lights[0]);

  // stats window
  XAC.stats = new Stats();
  XAC.stats.domElement.style.position = "absolute";
  XAC.stats.domElement.style.top = "0px";
  XAC.stats.domElement.style.right = "0px";
  $(document.body).append(XAC.stats.domElement);

  // renderer
  XAC.renderer = new THREE.WebGLRenderer({
    antialias: true
  });
  XAC.renderer.setSize(window.innerWidth, window.innerHeight);
  $(document.body).append(XAC.renderer.domElement);
  XAC.renderer.setClearColor(MINUET.bgColor);

  var render = function() {
    requestAnimationFrame(render);
    XAC.mouseCtrls.update();
    XAC.stats.update();
    XAC.lights[0].position.copy(XAC.camera.position);
    XAC.renderer.render(XAC.scene, XAC.camera);
  };

  render();
};

//
// recursively do animation
//
MINUET.animate = function() {
  var data = MINUET.dataLogs;
  MINUET.user.position.copy(data.position);
  XAC.scene.remove(MINUET.pointer);
  MINUET.pointer = addAnArrow(
    MINUET.user.position,
    data.orientation,
    1000,
    0xff0000,
    25
    );
  XAC.scene.add(MINUET.pointer);
};


