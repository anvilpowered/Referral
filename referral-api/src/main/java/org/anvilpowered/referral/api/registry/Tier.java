package org.anvilpowered.referral.api.registry;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;
import java.util.Map;

@ConfigSerializable
public class Tier {

    @Setting("tierName")
    public String tierName;

    @Setting("referralRequirement")
    public int referralRequirement;

    @Setting("economy")
    public int economy;

    @Setting("items")
    public List<Map<String, Integer>> items;

    @Setting("kit")
    public String kit;

    @Setting("permissions")
    public List<String> permissions;

    @Setting("commands")
    public List<String> commands;
}
