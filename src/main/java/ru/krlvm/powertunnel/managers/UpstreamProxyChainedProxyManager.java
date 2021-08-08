package ru.krlvm.powertunnel.managers;

import org.littleshoot.proxy.ChainedProxy;
import org.littleshoot.proxy.ChainedProxyManager;

import java.net.UnknownHostException;
import java.util.Queue;

import io.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.impl.ClientDetails;
import ru.krlvm.powertunnel.PowerTunnel;
import ru.krlvm.powertunnel.adapters.UpstreamChainedProxyAdapter;

public class UpstreamProxyChainedProxyManager implements ChainedProxyManager {

    private UpstreamChainedProxyAdapter adapter = null;

    public UpstreamProxyChainedProxyManager() {
        if(PowerTunnel.UPSTREAM_PROXY_CACHE) {
            try {
                adapter = new UpstreamChainedProxyAdapter(PowerTunnel.resolveUpstreamProxyAddress());
            } catch (UnknownHostException ex) {
                System.out.println("[x] Failed to cache upstream proxy address - resolution failed: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void lookupChainedProxies(HttpRequest httpRequest, Queue<ChainedProxy> chainedProxies, ClientDetails clientDetails) {
        chainedProxies.add(adapter != null ? adapter : new UpstreamChainedProxyAdapter());
    }
}
