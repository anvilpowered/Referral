package org.anvilpowered.referral.api.registry;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;
import java.util.Map;

@ConfigSerializable
public class Tier {

    @Setting
    public String name;

    @Setting
    public int referralRequirement;

    @Setting
    public int economy;

    @Setting
    public List<Map<String, Integer>> items;

    @Setting
    public String kit;

    @Setting
    public List<String> permissions;

    @Setting
    public List<String> commands;

    @Setting
    public int xp;
}
