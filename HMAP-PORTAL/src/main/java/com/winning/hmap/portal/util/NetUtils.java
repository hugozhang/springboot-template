package com.winning.hmap.portal.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 工具类.
 *
 * @auther: hugo.zxh
 * @date: 2022/10/18 10:26
 * @description:
 */
@Slf4j
public class NetUtils {

    private static volatile String LOCAL_MAC;

    private static final String DEFAULT_MAC = "E0-94-67-CE-04-60";

    /**
     * 获取本机的Mac地址
     *
     * @return 获取本机的Mac地址
     */
    public static String getLocalMac() {
        if (LOCAL_MAC != null) {
            return LOCAL_MAC;
        }
        try {
            LOCAL_MAC = getLocalMac0();
        } catch (SocketException e) {
            LOCAL_MAC = null;
            log.error(e.getMessage(),e);
        }
        return LOCAL_MAC;
    }

    private static String format(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (byte b : mac) {
            sb.append(String.format("%02X", b)).append("-");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private static String getLocalMac0() throws SocketException {

        List<String> list = new ArrayList<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Optional.ofNullable(networkInterface.getHardwareAddress()).ifPresent(mac -> list.add(format(mac)));
        }
        if (!list.isEmpty()) {
            return list.get(0).toUpperCase();
        }

//        //windows下获取mac地址
//        StringBuffer sb = new StringBuffer("");
//        InetAddress ia = InetAddress.getLocalHost();
//        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
//        int length = mac.length;
//        if (mac != null && length > 0) {
//            for(int i = 0; i< length; i++) {
//                if( i!=0 ) {
//                    sb.append("-");
//                }
//                //字节转换为整数
//                int temp = mac[i]&0xff;
//                String str = Integer.toHexString(temp);
//                if (str.length() == 1) {
//                    sb.append("0"+str);
//                } else {
//                    sb.append(str);
//                }
//            }
//        }
//
//
//        //unix下获取mac地址
//        if(StringUtils.isBlank(sb.toString())) {
//            String result = getUnixMACAddress();
//            if (StringUtils.isNotBlank(result)) {
//                return result;
//            }
//        } else {
//            return sb.toString().toUpperCase();
//        }
        //返回默认值
        return DEFAULT_MAC;
    }

    //unix下的mac地址
    private static String getUnixMACAddress() {
        String mac;
        BufferedReader bufferedReader = null;
        Process process;
        try {
            //mac os下的命令 一般取ether 作为本地主网卡 显示信息中包含有MAC地址信息
            process = Runtime.getRuntime().exec("ifconfig");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                index = line.toLowerCase().indexOf("ether");
                if (index >= 0) {
                    mac = line.substring(index + "ether".length() + 1).trim();
                    index = mac.toLowerCase().indexOf("hwaddr");// 寻找标示字符串[hwaddr]
                    if (index >= 0) {// 找到了
                        mac = mac.substring(index + "hwaddr".length() + 1).trim();// 取出mac地址并去除2边空格
                        return mac.toUpperCase();
                    }
                    break;
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
            bufferedReader = null;
            process = null;
        }
        return null;
    }


    private static volatile InetAddress LOCAL_ADDRESS = null;

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    public static String getLocalHost() {
        InetAddress address = getLocalAddress();
        return address == null ? "127.0.0.1" : address.getHostAddress();
    }

    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        } else {
            InetAddress localAddress = getLocalAddress0();
            LOCAL_ADDRESS = localAddress;
            return localAddress;
        }
    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;

        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable var6) {
            log.warn("Failed to retriving ip address, " + var6.getMessage(), var6);
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while(interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while(addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable var5) {
                                    log.warn("Failed to retriving ip address, " + var5.getMessage(), var5);
                                }
                            }
                        }
                    } catch (Throwable var7) {
                        log.warn("Failed to retriving ip address, " + var7.getMessage(), var7);
                    }
                }
            }
        } catch (Throwable var8) {
            log.warn("Failed to retriving ip address, " + var8.getMessage(), var8);
        }

        log.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address != null && !address.isLoopbackAddress()) {
            String name = address.getHostAddress();
            return name != null && !"0.0.0.0".equals(name) && !"127.0.0.1".equals(name) && IP_PATTERN.matcher(name).matches();
        } else {
            return false;
        }
    }
}
