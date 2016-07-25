package com.nosix.cloud.common;

public enum URLParam {
    id("id", "cloud"),
	version("version", "1.0.0"),
	group("group", "default"),
    transport("transport", "nifty"),
	proxy("proxy", "jdk"),
	cluster("cluster", Constants.SPI_NAME_DEFAULT),
	loadbalance("loadbalance", "failover"),
	haStrategy("haStrategy", "");

	private String name;
    private String value;
    private long longValue;
    private int intValue;
    private boolean boolValue;

    private URLParam(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private URLParam(String name, long longValue) {
        this.name = name;
        this.value = String.valueOf(longValue);
        this.longValue = longValue;
    }

    private URLParam(String name, int intValue) {
        this.name = name;
        this.value = String.valueOf(intValue);
        this.intValue = intValue;
    }

    private URLParam(String name, boolean boolValue) {
        this.name = name;
        this.value = String.valueOf(boolValue);
        this.boolValue = boolValue;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getIntValue() {
        return intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public boolean getBooleanValue() {
        return boolValue;
    }
}