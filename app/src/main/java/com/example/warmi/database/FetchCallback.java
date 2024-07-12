package com.example.warmi.database;

import com.example.warmi.models.address.AddressItems;
import com.example.warmi.models.address.AddressModel;
import com.example.warmi.models.PaymentsModel;
import com.example.warmi.models.orders.OrderModel;
import com.example.warmi.models.cart.CartItems;
import com.example.warmi.models.cart.CartModel;
import com.example.warmi.models.TransactionsModel;
import com.example.warmi.models.payment_methode.PaymentMethodeItems;
import com.example.warmi.models.payment_methode.PaymentMethodeModel;
import com.example.warmi.models.products.ProductItems;
import com.example.warmi.models.products.ProductsModel;
import com.example.warmi.models.UsersModel;

public class FetchCallback {
    public interface FetchUserCallback {
        void onUserFetched(UsersModel usersModel);

        void onError(Exception e);
    }

    public interface FetchAddressCallback {
        void onAddressFetched(AddressModel addressModel);

        void onError(Exception e);
    }

    public interface FetchAddressItemCallback {
        void onAddressItemFetched(AddressItems addressItems);

        void onError(Exception e);
    }

    public interface FetchProductCallback {
        void onProductFetched(ProductsModel productsModel);

        void onError(Exception e);
    }

    public interface FetchProductItemCallback {
        void onProductItemFetched(ProductItems productItems);

        void onError(Exception e);
    }

    public interface FetchCartCallback {
        void onCartFetched(CartModel cartModel);

        void onError(Exception e);
    }

    public interface FetchCartItemCallback {
        void onCartItemFetched(CartItems cartItems);

        void onError(Exception e);
    }

    public interface FetchOrderCallback {
        void onOrderFetched(OrderModel orderModel);

        void onError(Exception e);
    }

    public interface FetchPaymentMethodeCallback {
        void onPaymentMethodeFetched(PaymentMethodeModel paymentMethodeModel);

        void onError(Exception e);
    }

    public interface FetchPaymentMethodeItemCallback {
        void onPaymentMethodeItemFetched(PaymentMethodeItems paymentMethodeItems);

        void onError(Exception e);
    }

    public interface FetchPaymentCallback {
        void onPaymentFetched(PaymentsModel paymentsModel);

        void onError(Exception e);
    }

    public interface FetchTransactionsCallback {
        void onTransactionsFetched(TransactionsModel transactionsModel);

        void onError(Exception e);
    }

    public interface FetchTransactionsItemCallback {
        void onTransactionsItemFetched(TransactionsModel transactionsModel);

        void onError(Exception e);
    }
}
