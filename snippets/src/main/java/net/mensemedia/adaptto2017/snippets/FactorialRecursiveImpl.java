package net.mensemedia.adaptto2017.snippets;
class FactorialRecursiveImpl implements Factorial {
    @Override
    public long fac(final int n) {
        return n > 1 ? n * fac(n - 1) : 1;
    }
}
