package org.stormdev.mkTokens;

import net.stormdev.mario.server.EconProvider;

import org.bukkit.entity.Player;
import org.stormdev.gbapi.storm.tokens.Tokens.TokenServiceUnavailableException;
import org.stormdev.gbplugin.plugin.core.GameBlade;

public class TokenEcon implements EconProvider {

	@Override
	public double getBalance(Player player) { //MK calls async, so no worries
		try {
			return GameBlade.api.getTokenHandler().getTokens(player);
		} catch (TokenServiceUnavailableException e) {
			return 0;
		}
	}

	@Override
	public void spend(Player player, double amount) {
		int i  = (int) Math.ceil(amount); //Round up
		GameBlade.api.getTokenHandler().takeTokens(player, i);
	}

	@Override
	public void give(Player player, double amount) {
		GameBlade.plugin.getLogger().info("Awarded "+amount+" tokens to "+player.getName());
		int i = (int) Math.round(amount);
		GameBlade.api.getTokenHandler().awardTokens(player, i);
	}

}
