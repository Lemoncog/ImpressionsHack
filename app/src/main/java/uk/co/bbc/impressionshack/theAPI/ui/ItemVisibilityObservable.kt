package net.rossharper.impressionsexperiment.impressions.ui

interface ItemImpressionObserver<ItemDescriptorT> {
    fun onItemImpression(itemDescriptor: ItemDescriptorT)
}