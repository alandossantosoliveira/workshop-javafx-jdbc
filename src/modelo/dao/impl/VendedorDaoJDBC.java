package modelo.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import modelo.dao.VendedorDao;
import modelo.entidades.Departamento;
import modelo.entidades.Vendedor;

public class VendedorDaoJDBC implements VendedorDao {

	private Connection conn;

	public VendedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Vendedor obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO vendedor "
					+ "(nome, email, dataNascimento, salarioBase, id_departamento) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());
			
			int rowsAffetcted = st.executeUpdate();
			
			if(rowsAffetcted > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Vendedor obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE vendedor "
					+ "SET nome = ?, email = ?, dataNascimento = ?, salarioBase = ?, id_departamento = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM vendedor WHERE Id = ?");
			
			st.setInt(1, id);
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected == 0) {
				throw new DbException("Id not exist!");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}

	}

	@Override
	public Vendedor findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT vendedor.*,departamento.nome as Depnome " 
							+ "FROM vendedor INNER JOIN departamento "
							+ "ON vendedor.id_departamento = departamento.Id " 
							+ "WHERE vendedor.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				Departamento dep = instantiateDeparment(rs);
				Vendedor obj = instantiateVendedor(rs, dep);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private Vendedor instantiateVendedor(ResultSet rs, Departamento dep) throws SQLException {
		Vendedor obj = new Vendedor();
		obj.setId(rs.getInt("Id"));
		obj.setNome(rs.getString("nome"));
		obj.setEmail(rs.getString("email"));
		obj.setSalarioBase(rs.getDouble("salarioBase"));
		obj.setDataNascimento(rs.getDate("dataNascimento"));
		obj.setDepartamento(dep);

		return obj;
	}

	private Departamento instantiateDeparment(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("id_departamento"));
		dep.setNome(rs.getString("Depnome"));
		return dep;
	}

	@Override
	public List<Vendedor> findAll() {
		List<Vendedor> list = new ArrayList<>();

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT vendedor.*,departamento.nome as Depnome " + 
					 "FROM vendedor INNER JOIN departamento " +
					 "ON vendedor.id_departamento = departamento.Id " + 
					 "ORDER BY nome");

			rs = st.executeQuery();

			Map<Integer, Departamento> map = new HashMap<>();

			while (rs.next()) {

				Departamento dep = map.get(rs.getInt("id_departamento"));

				if (dep == null) {
					dep = instantiateDeparment(rs);
					map.put(rs.getInt("id_departamento"), dep);
				}

				Vendedor obj = instantiateVendedor(rs, dep);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Vendedor> findByDepartamento(Departamento departamento) {

		List<Vendedor> list = new ArrayList<>();

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT vendedor.*,departamento.nome as DepNome " + "FROM vendedor INNER JOIN departamento "
							+ "ON vendedor.id_departamento = departamento.id " + "WHERE id_departamento = ? ORDER BY nome");

			st.setInt(1, departamento.getId());

			rs = st.executeQuery();

			Map<Integer, Departamento> map = new HashMap<>();

			while (rs.next()) {

				Departamento dep = map.get(rs.getInt("id_departamento"));

				if (dep == null) {
					dep = instantiateDeparment(rs);
					map.put(rs.getInt("id_departamento"), dep);
				}

				Vendedor obj = instantiateVendedor(rs, dep);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
