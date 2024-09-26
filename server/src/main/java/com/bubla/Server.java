package com.bubla;

import com.bubla.classes.LinkedHashMapOfProducts;
import com.bubla.classes.Product;
import com.bubla.exceptions.StopServerException;
import com.bubla.executer.Executer;
import com.bubla.executer.ServerApplication;
import com.bubla.executer.ServerExecuter;
import com.bubla.message.Request;
import com.bubla.message.Response;
import com.google.common.primitives.Bytes;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.SerializationException;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

@Data
public class Server {
        private final int PACKET_SIZE = 1024;
        private final int DATA_SIZE = PACKET_SIZE - 1;
        private DatagramSocket ds;
        private InetAddress host;
        private int port = 3412;
        private SocketAddress addr;
        private boolean running = true;
        private ServerApplication application;

        public Server(InetAddress address, LinkedHashMap<String, Product> products) throws SocketException {
            this.host = address;
            this.addr = new InetSocketAddress(this.host, this.port);
            this.ds = new DatagramSocket(this.addr);
            this.ds.setReuseAddress(true);
            this.application = new ServerApplication(new LinkedHashMapOfProducts(products));
        }

        public Pair<Byte[], SocketAddress> receiveData() throws IOException {
            boolean recieved = false;
            byte[]result = new byte[0];
            SocketAddress address = null;

            while(!recieved){
                byte[] data = new byte[PACKET_SIZE];
                DatagramPacket dp = new DatagramPacket(data, PACKET_SIZE);
                ds.receive(dp);
                address = dp.getSocketAddress();
                if (data[data.length - 1] == 1){
                    recieved = true;
                }
                result = Bytes.concat(result, Arrays.copyOf(data, data.length - 1));
            }
            return new ImmutablePair<>(ArrayUtils.toObject(result), address);
        }

        public void sendData(byte[] data, SocketAddress address) throws IOException {
            byte[][] ret = new byte[(int) Math.ceil(data.length / (double) DATA_SIZE)][DATA_SIZE];

            int start = 0;
            for (int i = 0; i < ret.length; i++) {
                ret[i] = Arrays.copyOfRange(data, start, start + DATA_SIZE);
                start += DATA_SIZE;
            }


            for (int i = 0; i < ret.length; i++) {
                byte[] msg = ret[i];
                if (i == ret.length - 1) {
                    byte[] lastMsg = Bytes.concat(msg, new byte[]{1});
                    DatagramPacket dp = new DatagramPacket(lastMsg, PACKET_SIZE, address);
                    ds.send(dp);
                } else {
                    DatagramPacket dp = new DatagramPacket(ByteBuffer.allocate(PACKET_SIZE).put(msg).array(), PACKET_SIZE, address);
                    ds.send(dp);
                }
            }
        }

        public void connectToClient(SocketAddress addr) throws SocketException {
            ds.connect(addr);
        }

        public void disconnectFromClient() {
            ds.disconnect();
        }

        public void close() {
            ds.close();
        }

    public void run() {
            Executer executer = new Executer();
            new Thread(() -> {
                ServerExecuter serverExecuter = new ServerExecuter();
                Scanner sc = new Scanner(System.in);
                while(running){
                try{
                    System.out.println(serverExecuter.accomplish(sc.nextLine(), null, this.application));
                }catch (StopServerException e){
                    System.out.println(e.getMessage());
                    this.running = false;
                }
                }

            }).start();
            while (running) {
                Pair<Byte[], SocketAddress> dataPair;
                try {
                    dataPair = receiveData();
                } catch (Exception e) {
                    disconnectFromClient();
                    continue;
                }

                Byte[] dataFromClient = dataPair.getKey();
                int lastNotNull = dataFromClient.length;
                for (int i = 0; i < dataFromClient.length; i++) {
                    if (dataFromClient[i] != 0) {
                        lastNotNull = i;
                    }
                }

                SocketAddress clientAddr = dataPair.getValue();
                try {
                    connectToClient(clientAddr);
                } catch (Exception e) {
                }
                byte[] m = Arrays.copyOf(ArrayUtils.toPrimitive(dataFromClient), lastNotNull);
                Request request;
                try {
                    request = SerializationUtils.deserialize(Arrays.copyOf(ArrayUtils.toPrimitive(dataFromClient), lastNotNull + 1));
                } catch (SerializationException e) {
                    disconnectFromClient();
                    continue;
                }

                this.application.fillApplication(request.getNewProduct());
                String cmd = request.getCmd();
                String args = request.getArgs();
                String msg = executer.accomplish(cmd, args, this.application) + "\n";
                Response response = new Response(msg);
                response.setRunning(application.isRunnig());

                byte[] data = SerializationUtils.serialize(response);
                try {
                    sendData(data, clientAddr);
                } catch (Exception ignored) {
                }
                disconnectFromClient();
                this.application.setRunnig(true);
            }
            close();
    }
}
