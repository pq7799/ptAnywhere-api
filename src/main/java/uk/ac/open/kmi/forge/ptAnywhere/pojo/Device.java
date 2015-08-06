package uk.ac.open.kmi.forge.ptAnywhere.pojo;

import com.cisco.pt.ipc.sim.Cloud;
import com.cisco.pt.ipc.sim.Pc;
import com.cisco.pt.ipc.sim.Router;
import uk.ac.open.kmi.forge.ptAnywhere.api.http.AbstractWebRepresentable;
import uk.ac.open.kmi.forge.ptAnywhere.api.http.Utils;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement
public class Device extends AbstractWebRepresentable<Device> {
    // Shorter version of the identifier (URL) for vis.js
    // TODO check if this is useful at all regarding the widget performance.
    // This is the main identifier inside the webapp.
    String id;  // E.g., "b8d5exozT9eNsR1udGjbZQ--"
    String label;  // E.g., "MySwitch"
    double x;  // E.g., 436
    double y; // E.g., 345
    String group; // E.g., "switchDevice"
    List<Port> ports;

    // FIXME Only in Pcs and WirelessRouters!
    //@XmlElement(required=true)
    String defaultGateway;

    public Device() {
        this(null, null, -1, -1, null);
    }

    public Device(String id, String label, int x, int y, String group) {
        this.id = id;
        this.label = label;
        this.x = x;
        this.y = y;
        this.group = group;
        this.defaultGateway = null;
    }

    public static Device fromCiscoObject(com.cisco.pt.ipc.sim.Device device) {
        if (device==null) return null;
        final String id = Utils.toSimplifiedId(device.getObjectUUID());
        final String label = /*device.getClass() + ":" + device.getModel()
                               + ":" + */device.getName();
        final int deviceX = (int) (device.getXCoordinate()*1.5);
        final int deviceY = (int) (device.getYCoordinate()*1.5);
        final String group;
        String defaultGateway = null; // FIXME only defined when it is a pcDevice
        if (device instanceof Router) {
            group = "routerDevice";
        } else if (device instanceof Cloud) {
            group = "cloudDevice";
        } else if (device instanceof Pc) {
            group = "pcDevice";
            //defaultGateway = ((Pc) device).getDefaultGateway().getDottedQuadString();
            defaultGateway = "0.0.0.0";
        } else {
            group = "switchDevice";
        }
        final Device ret = new Device(id, label, deviceX, deviceY, group);
        ret.setDefaultGateway(defaultGateway);
        return ret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        if (this.uf==null) return null;
        return this.uf.createDeviceURL(this.id);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public String getDefaultGateway() {
        return defaultGateway;
    }

    public void setDefaultGateway(String defaultGateway) {
        this.defaultGateway = defaultGateway;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;

        Device device = (Device) o;

        if (!id.equals(device.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", group='" + group + '\'' +
                ", ports=" + ports +
                '}';
    }
}