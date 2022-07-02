package android.serialport.sample;
import android.serialport.sample.SubArea;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class AIS implements Serializable{

    Long dataSetId;
    String token;

    //静态信息
    String MMSI;
    //暂时作为主键，自己加的

    Integer shipid;
    String vesselName;
    Integer AISMessageType;
    Integer vesselType;

    //动态信息,航行相关
    Double speed;
    //经度
    Double longitude;
    //纬度
    Double latitude;
    Double rateOfTurn;
    //航向
    Double course;
    //对地航向（实际）
    Double heading;
    Integer state;
    Long timeStamp;

    String destination;
    String zone;
    Long eta;

    //用于构建地理信息索引
    double[] location;

    //新增用于储存当前道路的编号
    //Integer roadId;

    //安全相关
    Integer areaShape;
    Long startTime;
    Integer duration;

    List<SubArea> subAreas;

    public void setToken(String s) {
        token=s;
    }

    public void setAISMessageType(int i) {
        AISMessageType=i;
    }

    public void setMMSI(String testShip) {
        MMSI=testShip;
    }

    public void setAreaShape(int i) {
        areaShape=i;
    }


    public void setLocation(double[] loc) {
        location=loc;
    }


    public void setVesselName(String shipA) {
        vesselName=shipA;
    }


    public void setShipid(int i) {
        shipid=i;
    }

    public void setZone(String s) {
        zone=s;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setHeading(Double valueOf) {
        heading=valueOf;
    }
}
