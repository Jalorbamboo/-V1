package cn.bingoogolapple.baseadapter.databinding;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
public abstract class BgaBaseadapterItemDatabindingDummyBinding extends ViewDataBinding {
    // variables
    protected java.lang.Object mItemEventHandler;
    protected java.lang.Object mModel;
    protected cn.bingoogolapple.baseadapter.BGABindingViewHolder mViewHolder;
    protected BgaBaseadapterItemDatabindingDummyBinding(@Nullable android.databinding.DataBindingComponent bindingComponent, @Nullable android.view.View root_, int localFieldCount
    ) {
        super(bindingComponent, root_, localFieldCount);
    }
    //getters and abstract setters
    public abstract void setItemEventHandler(@Nullable java.lang.Object ItemEventHandler);
    @Nullable
    public java.lang.Object getItemEventHandler() {
        return mItemEventHandler;
    }
    public abstract void setModel(@Nullable java.lang.Object Model);
    @Nullable
    public java.lang.Object getModel() {
        return mModel;
    }
    public abstract void setViewHolder(@Nullable cn.bingoogolapple.baseadapter.BGABindingViewHolder ViewHolder);
    @Nullable
    public cn.bingoogolapple.baseadapter.BGABindingViewHolder getViewHolder() {
        return mViewHolder;
    }
    @NonNull
    public static BgaBaseadapterItemDatabindingDummyBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot) {
        return inflate(inflater, root, attachToRoot, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static BgaBaseadapterItemDatabindingDummyBinding inflate(@NonNull android.view.LayoutInflater inflater) {
        return inflate(inflater, android.databinding.DataBindingUtil.getDefaultComponent());
    }
    @NonNull
    public static BgaBaseadapterItemDatabindingDummyBinding bind(@NonNull android.view.View view) {
        return null;
    }
    @NonNull
    public static BgaBaseadapterItemDatabindingDummyBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.view.ViewGroup root, boolean attachToRoot, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    @NonNull
    public static BgaBaseadapterItemDatabindingDummyBinding inflate(@NonNull android.view.LayoutInflater inflater, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
    @NonNull
    public static BgaBaseadapterItemDatabindingDummyBinding bind(@NonNull android.view.View view, @Nullable android.databinding.DataBindingComponent bindingComponent) {
        return null;
    }
}