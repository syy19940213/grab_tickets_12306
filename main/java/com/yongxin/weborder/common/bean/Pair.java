package com.yongxin.weborder.common.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * 数据对
 * @author wangming 2013-03-20
 *
 * @param <T1>
 * @param <T2>
 */
public class Pair<T1, T2> implements Serializable
{
    private static final long serialVersionUID = -5473162399311701494L;

    public T1 left;

    public T2 right;

    public Pair()
    {
    }

    public Pair(T1 left, T2 right)
    {
        this.left = left;
        this.right = right;
    }

    public <F> void method(F param)
    {
    }

    @Override
    public String toString()
    {
        return "<" + this.left + "," + this.right + ">";
    }

    public T1 getLeft()
    {
        return this.left;
    }

    public T2 getRight()
    {
        return this.right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(left, pair.left) &&
                Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}

