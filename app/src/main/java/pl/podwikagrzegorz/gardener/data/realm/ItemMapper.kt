package pl.podwikagrzegorz.gardener.data.realm

interface ItemMapper<T, K> {
    fun fromRealm(itemRealm: K): T
}